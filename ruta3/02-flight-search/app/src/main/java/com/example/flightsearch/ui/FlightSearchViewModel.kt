package com.example.flightsearch.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Un vuelo es una combinación salida/destino; puede estar en favoritos. */
data class Flight(
    val departure: Airport,
    val destination: Airport,
    val isFavorite: Boolean
)

/** Todos los vuelos desde [departure] hacia el resto de aeropuertos. */
fun buildFlights(departure: Airport, airports: List<Airport>, favorites: List<Favorite>): List<Flight> =
    airports.filter { it.iataCode != departure.iataCode }.map { destination ->
        Flight(
            departure = departure,
            destination = destination,
            isFavorite = favorites.any {
                it.departureCode == departure.iataCode && it.destinationCode == destination.iataCode
            }
        )
    }

/** Convierte las filas de favoritos en vuelos con los datos de cada aeropuerto. */
fun buildFavoriteFlights(airports: List<Airport>, favorites: List<Favorite>): List<Flight> {
    val byCode = airports.associateBy { it.iataCode }
    return favorites.mapNotNull { favorite ->
        val departure = byCode[favorite.departureCode]
        val destination = byCode[favorite.destinationCode]
        if (departure != null && destination != null) Flight(departure, destination, true) else null
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModel(
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /** Texto del campo de búsqueda (estado de Compose para que el cursor no salte). */
    var query by mutableStateOf("")
        private set

    private val queryFlow = MutableStateFlow("")
    private val _selectedAirport = MutableStateFlow<Airport?>(null)
    val selectedAirport: StateFlow<Airport?> = _selectedAirport.asStateFlow()

    init {
        // Restaura la última búsqueda guardada en DataStore
        viewModelScope.launch {
            val saved = userPreferencesRepository.searchQuery.first()
            query = saved
            queryFlow.value = saved
        }
    }

    /** Sugerencias de autocompletado: se vuelven a consultar con cada letra. */
    val suggestions: StateFlow<List<Airport>> = queryFlow
        .flatMapLatest { q -> if (q.isBlank()) flowOf(emptyList()) else flightRepository.searchAirports(q.trim()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Vuelos desde el aeropuerto elegido, marcando los favoritos. */
    val flights: StateFlow<List<Flight>> = combine(
        _selectedAirport, flightRepository.getAllAirports(), flightRepository.getAllFavorites()
    ) { selected, airports, favorites ->
        if (selected == null) emptyList() else buildFlights(selected, airports, favorites)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Rutas favoritas (se muestran cuando el campo de búsqueda está vacío). */
    val favoriteFlights: StateFlow<List<Flight>> = combine(
        flightRepository.getAllAirports(), flightRepository.getAllFavorites()
    ) { airports, favorites -> buildFavoriteFlights(airports, favorites) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(newQuery: String) {
        query = newQuery
        queryFlow.value = newQuery
        _selectedAirport.value = null
        viewModelScope.launch { userPreferencesRepository.saveSearchQuery(newQuery) }
    }

    fun onAirportSelected(airport: Airport) {
        _selectedAirport.value = airport
    }

    fun toggleFavorite(flight: Flight) {
        viewModelScope.launch {
            if (flight.isFavorite) {
                flightRepository.removeFavorite(flight.departure.iataCode, flight.destination.iataCode)
            } else {
                flightRepository.addFavorite(flight.departure.iataCode, flight.destination.iataCode)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as FlightSearchApplication)
                FlightSearchViewModel(app.container.flightRepository, app.container.userPreferencesRepository)
            }
        }
    }
}
