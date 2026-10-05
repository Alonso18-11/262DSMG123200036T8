package com.example.flightsearch

import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.buildFavoriteFlights
import com.example.flightsearch.ui.buildFlights
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FlightBuilderTest {
    private val lim = Airport(1, "LIM", "Jorge Chávez", 100)
    private val cuz = Airport(2, "CUZ", "Velasco Astete", 50)
    private val aqp = Airport(3, "AQP", "Rodríguez Ballón", 30)
    private val airports = listOf(lim, cuz, aqp)

    @Test
    fun buildFlights_excludesDepartureAirport() {
        val flights = buildFlights(lim, airports, emptyList())
        assertEquals(listOf("CUZ", "AQP"), flights.map { it.destination.iataCode })
    }

    @Test
    fun buildFlights_marksFavorites() {
        val flights = buildFlights(lim, airports, listOf(Favorite(1, "LIM", "CUZ")))
        assertTrue(flights.first { it.destination == cuz }.isFavorite)
        assertFalse(flights.first { it.destination == aqp }.isFavorite)
    }

    @Test
    fun buildFavoriteFlights_mapsCodesToAirports() {
        val favorites = buildFavoriteFlights(airports, listOf(Favorite(1, "CUZ", "AQP")))
        assertEquals(1, favorites.size)
        assertEquals(cuz, favorites[0].departure)
        assertEquals(aqp, favorites[0].destination)
    }
}
