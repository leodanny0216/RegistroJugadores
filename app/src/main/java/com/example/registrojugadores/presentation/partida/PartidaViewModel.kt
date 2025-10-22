package com.example.registrojugadores.presentation.partida

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadores.data.local.entity.PartidaEntity
import com.example.registrojugadores.data.remote.dto.MovimientoDto
import com.example.registrojugadores.data.repository.MovimientoRepository
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
    private val partidasRepository: PartidasRepository,
    private val movimientoRepository: MovimientoRepository
) : ViewModel() {

    private val _partidas = MutableStateFlow<List<PartidaEntity>>(emptyList())
    val partidas: StateFlow<List<PartidaEntity>> = _partidas.asStateFlow()

    private val _gameState = MutableStateFlow(GameUiState())
    val gameState: StateFlow<GameUiState> = _gameState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            partidasRepository.getAll().collect { lista ->
                _partidas.value = lista
            }
        }
    }

    fun loadGameStateFromPartida(partidaId: Int) {
        viewModelScope.launch {
            val partida = getPartidaById(partidaId)
            if (partida != null) {
                val movimientos = movimientoRepository.getMovimientosByPartida(partidaId)

                val board = MutableList<Player?>(9) { null }
                var currentPlayer = Player.X

                movimientos.forEach { m ->
                    val index = m.posicionFila * 3 + m.posicionColumna
                    board[index] = if (m.jugador == "Jugador 1") Player.X else Player.O
                    currentPlayer = if (m.jugador == "Jugador 1") Player.O else Player.X
                }

                val winner = checkWinner(board)
                val isDraw = board.all { it != null } && winner == null

                _gameState.value = GameUiState(
                    board = board,
                    currentPlayer = currentPlayer,
                    winner = winner,
                    isDraw = isDraw,
                    gameStarted = true,
                    jugador1Id = partida.jugador1Id ?: 0,
                    jugador2Id = partida.jugador2Id ?: 0
                )
            } else {
                _errorMessage.value = "No se encontró la partida con ID $partidaId"
            }
        }
    }

    fun savePartida(
        fecha: Date,
        jugador1Id: Int?,
        jugador2Id: Int?,
        ganadorId: Int?,
        esFinalizada: Boolean,
        id: Int? = null
    ) {
        viewModelScope.launch {
            if (jugador1Id == jugador2Id) {
                _errorMessage.value = "Los jugadores no pueden ser el mismo."
                return@launch
            }

            if (esFinalizada && ganadorId == null) {
                _errorMessage.value = "No se puede finalizar la partida sin un ganador."
                return@launch
            }

            val partida = PartidaEntity(
                partidaId = id,
                fecha = fecha,
                jugador1Id = jugador1Id,
                jugador2Id = jugador2Id,
                ganadorId = ganadorId,
                esFinalizada = esFinalizada
            )

            partidasRepository.save(partida)
            _errorMessage.value = null
        }
    }

    fun startGame(jugador1Id: Int?, jugador2Id: Int?) {
        if (jugador1Id == null || jugador2Id == null) {
            _errorMessage.value = "Debe seleccionar ambos jugadores antes de iniciar."
            return
        }

        if (jugador1Id == jugador2Id) {
            _errorMessage.value = "Los jugadores no pueden ser el mismo."
            return
        }

        _gameState.update {
            it.copy(
                gameStarted = true,
                jugador1Id = jugador1Id,
                jugador2Id = jugador2Id
            )
        }
        _errorMessage.value = null
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun onCellClick(index: Int) {
        val state = _gameState.value
        if (state.board[index] != null || state.winner != null || !state.gameStarted) return

        val newBoard = state.board.toMutableList()
        newBoard[index] = state.currentPlayer

        saveMovimientoToApi(index, state.currentPlayer)

        val newWinner = checkWinner(newBoard)
        val isDraw = newBoard.all { it != null } && newWinner == null

        _gameState.update {
            it.copy(
                board = newBoard,
                currentPlayer = if (it.currentPlayer == Player.X) Player.O else Player.X,
                winner = newWinner,
                isDraw = isDraw
            )
        }

        if (newWinner != null || isDraw) {
            val ganadorId = when (newWinner) {
                Player.X -> state.jugador1Id
                Player.O -> state.jugador2Id
                null -> null
            }

            savePartida(
                fecha = Date(),
                jugador1Id = state.jugador1Id,
                jugador2Id = state.jugador2Id,
                ganadorId = ganadorId,
                esFinalizada = true
            )
        }
    }

    private fun saveMovimientoToApi(index: Int, player: Player) {
        viewModelScope.launch {
            try {
                val fila = index / 3
                val columna = index % 3
                val movimiento = MovimientoDto(
                    movimientoId = 0,
                    jugador = if (player == Player.X) "Jugador 1" else "Jugador 2",
                    posicionFila = fila,
                    posicionColumna = columna
                )
                movimientoRepository.sendMovimiento(movimiento)
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrar movimiento: ${e.message}"
            }
        }
    }

    fun restartGame() {
        _gameState.value = GameUiState(
            jugador1Id = _gameState.value.jugador1Id,
            jugador2Id = _gameState.value.jugador2Id
        )
    }

    fun refreshPartidas() {
        viewModelScope.launch {
            partidasRepository.getAll().collect { lista ->
                _partidas.value = lista
            }
        }
    }

    private fun checkWinner(board: List<Player?>): Player? {
        val winningLines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for (line in winningLines) {
            val (a, b, c) = line
            if (board[a] != null && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }
        return null
    }

    fun deletePartida(partida: PartidaEntity) {
        viewModelScope.launch { partidasRepository.delete(partida) }
    }

    fun getPartidaById(id: Int?): PartidaEntity? =
        _partidas.value.find { it.partidaId == id }
}

enum class Player(val symbol: String) {
    X("X"),
    O("O")
}

