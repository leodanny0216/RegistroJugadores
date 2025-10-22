package com.example.registrojugadores.data.remote

import com.example.registrojugadores.data.remote.dto.MovimientoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MovimientoApi {

    // 🔹 Obtener lista de movimientos de una partida (GET)
    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") partidaId: Int): List<MovimientoDto>

    // 🔹 Registrar un nuevo movimiento (POST)
    @POST("api/Movimientos")
    suspend fun createMovimiento(@Body movimiento: MovimientoDto): Response<MovimientoDto>
}