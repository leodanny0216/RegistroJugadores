package com.example.registrojugadores.presentation.partida

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
