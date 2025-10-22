package com.example.registrojugadores.data.remote

import com.example.registrojugadores.data.remote.dto.MovimientoDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: MovimientoApi
) {
    suspend fun getMovimientos(id: Int): List<MovimientoDto> = api.getMovimientos(id)

    suspend fun createMovimiento(movimiento: MovimientoDto) = api.createMovimiento(movimiento)
}