package com.example.busschedule

import android.app.Application
import com.example.busschedule.data.AppDatabase

class BusScheduleApplication : Application() {
    /** La base se crea la primera vez que se usa. */
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
