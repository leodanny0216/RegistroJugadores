package com.example.registrojugadores.presentation.jugadores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadores.data.local.entity.JugadorEntity
import com.example.registrojugadores.data.repository.JugadoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadorViewModel @Inject constructor(
    private val repository: JugadoresRepository
) : ViewModel() {

    val jugadorList: StateFlow<List<JugadorEntity>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    fun saveJugador(nombres: String, partidas: Int, id: Int? = null) {
        viewModelScope.launch {
            val jugador = JugadorEntity(
                JugadorId = id,
                Nombres = nombres,
                Partidas = partidas
            )
            repository.save(jugador)
        }
    }

    fun delete(jugador: JugadorEntity) {
        viewModelScope.launch {
            repository.delete(jugador)
        }
    }

    fun update(jugador: JugadorEntity) {
        viewModelScope.launch {
            repository.save(jugador)
        }
    }
    fun getJugadorById(id: Int?): JugadorEntity? {
        return jugadorList.value.find { it.JugadorId == id }
    }
}