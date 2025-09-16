package com.example.registrojugadores.data.repository

import com.example.registrojugadores.data.local.dao.PartidaDao
import com.example.registrojugadores.data.local.entity.PartidaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PartidasRepository @Inject constructor(
    private val partidaDao: PartidaDao
) {
    suspend fun save(partida: PartidaEntity) = partidaDao.save(partida)
    suspend fun find(id: Int): PartidaEntity? = partidaDao.find(id)
    suspend fun delete(partida: PartidaEntity) = partidaDao.delete(partida)
    fun getAll(): Flow<List<PartidaEntity>> = partidaDao.getAll()
}
