package com.example.registrojugadores.presentation.partida

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.registrojugadores.data.local.entity.PartidaEntity
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaScreen(
    partida: PartidaEntity? = null,
    viewModel: PartidaViewModel = hiltViewModel(),
    onCancel: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()

    var showGame by remember { mutableStateOf(false) }
    var jugador1Id by remember { mutableStateOf(partida?.jugador1Id?.toString() ?: "") }
    var jugador2Id by remember { mutableStateOf(partida?.jugador2Id?.toString() ?: "") }
    var errorJugador1 by remember { mutableStateOf<String?>(null) }
    var errorJugador2 by remember { mutableStateOf<String?>(null) }

    if (showGame) {
        // Pantalla del juego
        GameScreen(
            gameState = gameState,
            onCellClick = viewModel::onCellClick,
            onRestartGame = viewModel::restartGame,
            onBack = { showGame = false }
        )
    } else {
        // Pantalla de configuración de la partida
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Configurar Partida",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Gray,
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = onCancel) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D47A1), Color(0xFF0D47A1))
                        )
                    )
                    .padding(padding)
                    .padding(20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Gray.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text("Seleccionar Jugadores", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = jugador1Id,
                        onValueChange = { jugador1Id = it },
                        label = { Text("ID Jugador 1") },
                        isError = errorJugador1 != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    errorJugador1?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }

                    OutlinedTextField(
                        value = jugador2Id,
                        onValueChange = { jugador2Id = it },
                        label = { Text("ID Jugador 2") },
                        isError = errorJugador2 != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    errorJugador2?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }

                    Text("Jugador 1 será X, Jugador 2 será O", fontSize = 16.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                val j1 = jugador1Id.toIntOrNull()
                                val j2 = jugador2Id.toIntOrNull()

                                if (j1 == null) errorJugador1 = "ID Jugador 1 requerido"
                                else errorJugador1 = null

                                if (j2 == null) errorJugador2 = "ID Jugador 2 requerido"
                                else errorJugador2 = null

                                if (j1 != null && j2 != null) {
                                    viewModel.startGame(j1, j2)
                                    showGame = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Iniciar Juego", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Iniciar Juego")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameState: GameUiState,
    onCellClick: (Int) -> Unit,
    onRestartGame: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tic-Tac-Toe",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF0D47A1))
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GameBoard(
                uiState = gameState,
                onCellClick = onCellClick,
                onRestartGame = onRestartGame
            )
        }
    }
}

@Composable
fun GameBoard(
    uiState: GameUiState,
    onCellClick: (Int) -> Unit,
    onRestartGame: () -> Unit
) {
    val gameStatus = when {
        uiState.winner != null -> "🏆 ¡Ganador: ${uiState.winner.symbol}!"
        uiState.isDraw -> "🤝 ¡Es un empate!"
        else -> "Turno de: ${uiState.currentPlayer.symbol}"
    }

    Text(text = gameStatus, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
    Spacer(modifier = Modifier.height(20.dp))

    Column {
        (0..2).forEach { row ->
            Row {
                (0..2).forEach { col ->
                    val index = row * 3 + col
                    BoardCell(uiState.board[index]) {
                        onCellClick(index)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = onRestartGame,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
    ) {
        Text("Reiniciar Juego", fontSize = 18.sp)
    }
}

@Composable
private fun BoardCell(
    player: Player?,
    onCellClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(Color.LightGray)
            .clickable { onCellClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = player?.symbol ?: "",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = if (player == Player.X) Color.Blue else Color.Red
        )
    }
}