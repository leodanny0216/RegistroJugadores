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

    // Estado para el juego Tic-Tac-Toe
    private val _gameState = MutableStateFlow(GameUiState())
    val gameState: StateFlow<GameUiState> = _gameState.asStateFlow()

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

    // Funciones para el juego Tic-Tac-Toe
    fun selectPlayer(player: Player) {
        _gameState.update { it.copy(playerSelection = player) }
    }

    fun startGame(jugador1Id: Int?, jugador2Id: Int?) {
        _gameState.update {
            it.copy(
                gameStarted = true,
                jugador1Id = jugador1Id ?: 0, // Si es null, asigna 0
                jugador2Id = jugador2Id ?: 0  // Si es null, asigna 0
            )
        }
    }


    fun onCellClick(index: Int) {
        if (_gameState.value.board[index] != null || _gameState.value.winner != null) {
            return
        }

        val newBoard = _gameState.value.board.toMutableList()
        newBoard[index] = _gameState.value.currentPlayer

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

        // Si hay un ganador o empate, guardar la partida automáticamente
        if (newWinner != null || isDraw) {
            val ganadorId = when (newWinner) {
                Player.X -> _gameState.value.jugador1Id
                Player.O -> _gameState.value.jugador2Id
                null -> null
            }

            savePartida(
                fecha = Date(),
                jugador1Id = _gameState.value.jugador1Id,
                jugador2Id = _gameState.value.jugador2Id,
                ganadorId = ganadorId,
                esFinalizada = true
            )
        }
    }

    fun restartGame() {
        _gameState.value = GameUiState(
            jugador1Id = _gameState.value.jugador1Id,
            jugador2Id = _gameState.value.jugador2Id
        )
    }

    private fun checkWinner(board: List<Player?>): Player? {
        val winningLines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Horizontales
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Verticales
            listOf(0, 4, 8), listOf(2, 4, 6) // Diagonales
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
        viewModelScope.launch {
            repository.delete(partida)
        }
    }

    fun getPartidaById(id: Int?): PartidaEntity? {
        return _partidas.value.find { it.partidaId == id }
    }
}

enum class Player(val symbol: String) {
    X("X"),
    O("O")
}

data class GameUiState(
    val board: List<Player?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false,
    val playerSelection: Player? = null,
    val gameStarted: Boolean = false,
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0
)