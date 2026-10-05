package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun searchAirports(query: String): Flow<List<Airport>>
    fun getAllAirports(): Flow<List<Airport>>
    fun getAllFavorites(): Flow<List<Favorite>>
    suspend fun addFavorite(departureCode: String, destinationCode: String)
    suspend fun removeFavorite(departureCode: String, destinationCode: String)
}

class OfflineFlightRepository(private val dao: FlightDao) : FlightRepository {
    override fun searchAirports(query: String) = dao.searchAirports(query)
    override fun getAllAirports() = dao.getAllAirports()
    override fun getAllFavorites() = dao.getAllFavorites()
    override suspend fun addFavorite(departureCode: String, destinationCode: String) =
        dao.insertFavorite(Favorite(departureCode = departureCode, destinationCode = destinationCode))
    override suspend fun removeFavorite(departureCode: String, destinationCode: String) =
        dao.deleteFavorite(departureCode, destinationCode)
}
