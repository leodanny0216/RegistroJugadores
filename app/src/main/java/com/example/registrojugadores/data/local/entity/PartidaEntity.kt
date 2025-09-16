package com.example.registrojugadores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Partidas")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true)
    val partidaId: Int? = null,
    val fecha: Date = Date(),
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false
)