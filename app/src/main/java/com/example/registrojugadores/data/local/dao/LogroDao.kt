package com.example.registrojugadores.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.registrojugadores.data.local.entity.LogroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {
    @Upsert
    suspend fun save(logro: LogroEntity)

    @Query("SELECT * FROM Logros WHERE logroId = :id LIMIT 1")
    suspend fun find(id: Int): LogroEntity?

    @Delete
    suspend fun delete(logro: LogroEntity)

    @Query("SELECT * FROM Logros")
    fun getAll(): Flow<List<LogroEntity>>
}