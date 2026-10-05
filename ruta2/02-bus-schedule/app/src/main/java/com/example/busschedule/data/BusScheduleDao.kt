package com.example.busschedule.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusScheduleDao {
    /** Horario completo ordenado por hora de llegada. */
    @Query("SELECT * FROM Schedule ORDER BY arrival_time ASC")
    fun getAll(): Flow<List<BusSchedule>>

    /** Horario de un solo paradero. */
    @Query("SELECT * FROM Schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
    fun getByStopName(stopName: String): Flow<List<BusSchedule>>
}
