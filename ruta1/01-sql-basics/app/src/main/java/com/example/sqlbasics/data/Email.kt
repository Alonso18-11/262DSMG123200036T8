package com.example.sqlbasics.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Cada fila de la tabla "email". */
@Entity(tableName = "email")
data class Email(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "sender") val sender: String,
    @ColumnInfo(name = "folder") val folder: String,
    @ColumnInfo(name = "starred") val starred: Boolean,
    @ColumnInfo(name = "read") val read: Boolean,
    @ColumnInfo(name = "received") val received: Int
)
