package com.example.registrojugadores.presentation.partida

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadores.data.local.entity.PartidaEntity
import com.example.registrojugadores.data.repository.PartidasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PartidaViewModel @Inject constructor(
    private val repository: PartidasRepository
) : ViewModel() {

    private val _partidas = MutableStateFlow<List<PartidaEntity>>(emptyList())
    val partidas: StateFlow<List<PartidaEntity>> = _partidas.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAll().collect { lista ->
                _partidas.value = lista
            }
        }
    }


    fun savePartida(
        fecha: Date,
        jugador1Id: Int,
        jugador2Id: Int,
        ganadorId: Int?,
        esFinalizada: Boolean,
        id: Int? = null
    ) {
        viewModelScope.launch {
            val partida = PartidaEntity(
                partidaId = id,
                fecha = fecha,
                jugador1Id = jugador1Id,
                jugador2Id = jugador2Id,
                ganadorId = ganadorId,
                esFinalizada = esFinalizada
            )
            repository.save(partida)
        }
    }

    fun deletePartida(partida: PartidaEntity) {
        viewModelScope.launch {
            repository.delete(partida)
        }
    }

    fun updatePartida(partida: PartidaEntity) {
        viewModelScope.launch {
            repository.save(partida)
        }
    }

    fun getPartidaById(id: Int?): PartidaEntity? {
        return _partidas.value.find { it.partidaId == id }
    }
}
