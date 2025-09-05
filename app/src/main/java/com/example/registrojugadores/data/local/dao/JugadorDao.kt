package com.example.registrojugadores.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.registrojugadores.data.local.entity.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Upsert
    suspend fun save(jugador: JugadorEntity)

    @Query("SELECT * FROM Jugadores WHERE JugadorId = :id LIMIT 1")
    suspend fun find(id: Int): JugadorEntity?

    @Query("SELECT * FROM Jugadores WHERE Nombres = :nombres LIMIT 1")
    suspend fun findByName(nombres: String): JugadorEntity?

    @Delete
    suspend fun delete(jugador: JugadorEntity)

    @Query("SELECT * FROM Jugadores")
    fun getAll(): Flow<List<JugadorEntity>>
}