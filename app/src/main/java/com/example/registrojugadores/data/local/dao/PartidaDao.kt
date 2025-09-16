package com.example.registrojugadores.data.local.dao

import androidx.room.*
import com.example.registrojugadores.data.local.entity.PartidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Upsert
    suspend fun save(partida: PartidaEntity)

    @Query("SELECT * FROM Partidas WHERE partidaId = :id LIMIT 1")
    suspend fun find(id: Int): PartidaEntity?

    @Delete
    suspend fun delete(partida: PartidaEntity)

    @Query("SELECT * FROM Partidas")
    fun getAll(): Flow<List<PartidaEntity>>
}