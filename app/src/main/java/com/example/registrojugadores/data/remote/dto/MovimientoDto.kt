package com.example.registrojugadores.data.remote.dto

data class MovimientoDto(
    val movimientoId: Int,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int
)