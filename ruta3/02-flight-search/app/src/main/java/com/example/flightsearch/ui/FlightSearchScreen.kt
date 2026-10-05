@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun FlightSearchApp(viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)) {
    val suggestions by viewModel.suggestions.collectAsState()
    val selectedAirport by viewModel.selectedAirport.collectAsState()
    val flights by viewModel.flights.collectAsState()
    val favorites by viewModel.favoriteFlights.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = viewModel.query,
                onValueChange = viewModel::onQueryChange,
                singleLine = true,
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (viewModel.query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear))
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            val airport = selectedAirport
            when {
                // 1) Hay aeropuerto elegido: lista de vuelos
                airport != null -> FlightList(
                    title = stringResource(R.string.flights_from, airport.iataCode),
                    flights = flights,
                    onFavoriteClick = viewModel::toggleFavorite
                )
                // 2) Campo vacío: rutas favoritas
                viewModel.query.isBlank() -> {
                    if (favorites.isEmpty()) {
                        CenteredMessage(stringResource(R.string.no_favorites))
                    } else {
                        FlightList(
                            title = stringResource(R.string.favorite_routes),
                            flights = favorites,
                            onFavoriteClick = viewModel::toggleFavorite
                        )
                    }
                }
                // 3) Escribiendo: sugerencias de autocompletado
                else -> {
                    if (suggestions.isEmpty()) {
                        CenteredMessage(stringResource(R.string.no_results))
                    } else {
                        SuggestionList(
                            airports = suggestions,
                            onAirportClick = {
                                focusManager.clearFocus()
                                viewModel.onAirportSelected(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CenteredMessage(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    )
}

@Composable
fun SuggestionList(airports: List<Airport>, onAirportClick: (Airport) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(airports, key = { it.id }) { airport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAirportClick(airport) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AirportLabel(airport)
            }
        }
    }
}

@Composable
fun FlightList(
    title: String,
    flights: List<Flight>,
    onFavoriteClick: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(flights, key = { "${it.departure.iataCode}-${it.destination.iataCode}" }) { flight ->
                FlightCard(flight = flight, onFavoriteClick = { onFavoriteClick(flight) })
            }
        }
    }
}

@Composable
fun FlightCard(flight: Flight, onFavoriteClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.depart), style = MaterialTheme.typography.labelSmall)
                AirportLabel(flight.departure)
                Text(stringResource(R.string.arrive), style = MaterialTheme.typography.labelSmall)
                AirportLabel(flight.destination)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(
                        if (flight.isFavorite) R.string.remove_favorite else R.string.add_favorite
                    ),
                    tint = if (flight.isFavorite) Color(0xFFFFB300) else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun AirportLabel(airport: Airport) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(airport.iataCode, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Text(airport.name, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun FlightCardPreview() {
    FlightSearchTheme {
        FlightCard(
            Flight(Airport(1, "LIM", "Jorge Chávez", 1), Airport(2, "CUZ", "Velasco Astete", 1), true),
            onFavoriteClick = {}
        )
    }
}
