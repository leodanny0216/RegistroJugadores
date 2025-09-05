package com.example.registrojugadores.presentation.jugadores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadores.data.local.entity.JugadorEntity
import com.example.registrojugadores.data.repository.JugadoresRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class JugadorViewModel @Inject constructor(
    private val repository: JugadoresRepository
) : ViewModel() {

    val jugadorList: StateFlow<List<JugadorEntity>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregarJugador(nombres: String, partidas: Int) {
        viewModelScope.launch {
            val jugador = JugadorEntity(
                Nombres = nombres,
                Partidas = partidas
            )
            saveJugador(jugador)
        }
    }

    fun saveJugador(jugador: JugadorEntity) {
        viewModelScope.launch {
            repository.save(jugador)
        }
    }

    fun delete(jugador: JugadorEntity) {
        viewModelScope.launch {
            repository.delete(jugador)
        }
    }

    fun update(jugador: JugadorEntity) {
        saveJugador(jugador)
    }

    fun getJugadorById(id: Int?): JugadorEntity? {
        return jugadorList.value.find { it.JugadorId == id }
    }
}