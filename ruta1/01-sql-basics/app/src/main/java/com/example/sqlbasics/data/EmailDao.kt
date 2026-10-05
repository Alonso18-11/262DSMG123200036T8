package com.example.sqlbasics.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailDao {
    /** Misma consulta del codelab: los más recientes primero. */
    @Query("SELECT * FROM email ORDER BY received DESC")
    fun getAll(): Flow<List<Email>>
}
