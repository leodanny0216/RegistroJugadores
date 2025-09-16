package com.example.registrojugadores.presentation.partida

import com.example.registrojugadores.data.local.entity.PartidaEntity
import java.util.Date

data class PartidaUiState(
    val partidaId: Int? = null,
    val fecha: Date = Date(),
    val jugador1Id: Int? = null,
    val jugador2Id: Int? = null,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false,
    val partidas: List<PartidaEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
