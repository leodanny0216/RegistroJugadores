package com.example.registrojugadores.data.repository

import com.example.registrojugadores.data.local.dao.JugadorDao
import com.example.registrojugadores.data.local.entity.JugadorEntity
import kotlinx.coroutines.flow.Flow

class JugadoresRepository(
    private val dao: JugadorDao
) {
    suspend fun save(jugador: JugadorEntity): Result<Unit> {
        val existing = dao.findByName(jugador.Nombres)
        return if (existing != null && existing.JugadorId != jugador.JugadorId) {
            Result.failure(Exception("Ya existe un jugador con este nombre"))
        } else {
            dao.save(jugador)
            Result.success(Unit)
        }
    }

    suspend fun find(id: Int): JugadorEntity? = dao.find(id)

    suspend fun delete(jugador: JugadorEntity) = dao.delete(jugador)

    fun getAll(): Flow<List<JugadorEntity>> = dao.getAll()
}