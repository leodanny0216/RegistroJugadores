package com.example.registrojugadores.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registrojugadores.data.local.dao.JugadorDao
import com.example.registrojugadores.data.local.entity.JugadorEntity

@Database(
    entities = [
        JugadorEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class JugadorDb : RoomDatabase() {
    abstract fun JugadorDao(): JugadorDao
}