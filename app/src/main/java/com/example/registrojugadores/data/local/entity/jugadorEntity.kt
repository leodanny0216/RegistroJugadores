package com.example.registrojugadores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Jugadores")
data class JugadorEntity(
    @PrimaryKey
    val JugadorId: Int? = null,
    val Nombres: String = "",
    val Partidas: Int = 0
)