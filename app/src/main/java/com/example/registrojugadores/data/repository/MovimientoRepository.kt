package com.example.registrojugadores.data.repository

import com.example.registrojugadores.data.remote.RemoteDataSource
import com.example.registrojugadores.data.remote.dto.MovimientoDto
import javax.inject.Inject

class MovimientoRepository @Inject constructor(
    private val remote: RemoteDataSource
) {
    // Función que obtiene movimientos por partida
    suspend fun getMovimientosByPartida(partidaId: Int): List<MovimientoDto> =
        remote.getMovimientos(partidaId)

    // Función que envía un nuevo movimiento
    suspend fun sendMovimiento(movimiento: MovimientoDto) =
        remote.createMovimiento(movimiento)
}
