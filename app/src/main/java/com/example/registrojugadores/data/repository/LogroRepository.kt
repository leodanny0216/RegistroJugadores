package com.example.registrojugadores.data.repository

import com.example.registrojugadores.data.local.dao.LogroDao
import com.example.registrojugadores.data.local.entity.LogroEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogroRepository @Inject constructor(
    private val dao: LogroDao
) {
    suspend fun saveLogro(logro: LogroEntity) = dao.save(logro)

    suspend fun find(id: Int): LogroEntity? = dao.find(id)

    suspend fun delete(logro: LogroEntity) = dao.delete(logro)

    fun getAll(): Flow<List<LogroEntity>> = dao.getAll()
}