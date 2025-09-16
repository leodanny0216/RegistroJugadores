package com.example.registrojugadores.presentation.navegation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object JugadorList : Screen()
    @Serializable
    data class Jugador(val jugadorId: Int?) : Screen()
    @Serializable
    data object PartidaList : Screen()

    @Serializable
    data class Partida(val partidaId: Int?) : Screen()

    @Serializable
    data object Dashboard:Screen()
}