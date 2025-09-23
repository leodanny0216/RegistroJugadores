package com.example.registrojugadores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Logros")
data class LogroEntity(
    @PrimaryKey(autoGenerate = true)
    val logroId: Int? = null,
    val fecha: Date,
    val jugadorId: Int,
    val partidaId: Int? = null,
    val descripcion: String
)