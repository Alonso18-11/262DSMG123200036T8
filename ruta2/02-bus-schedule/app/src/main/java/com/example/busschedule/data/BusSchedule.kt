package com.example.busschedule.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Fila de la tabla "Schedule" de la base precargada. */
@Entity(tableName = "Schedule")
data class BusSchedule(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "stop_name")
    val stopName: String,
    @ColumnInfo(name = "arrival_time")
    val arrivalTimeInMillis: Int // en realidad son segundos Unix, como en el codelab
)
