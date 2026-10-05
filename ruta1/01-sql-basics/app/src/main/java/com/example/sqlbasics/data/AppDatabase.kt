package com.example.sqlbasics.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * La base de datos se crea a partir del archivo assets/database/email.db,
 * que ya trae la tabla email con datos de ejemplo.
 */
@Database(entities = [Email::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emailDao(): EmailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "email_database")
                    .createFromAsset("database/email.db")
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
