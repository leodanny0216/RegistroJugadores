package com.example.registrojugadores.presentation.partida

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.registrojugadores.data.local.entity.JugadorEntity
import com.example.registrojugadores.data.local.entity.PartidaEntity
import com.example.registrojugadores.presentation.jugadores.JugadorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaScreen(
    navController: NavController,
    partidaId: Int? = null,
    partidaViewModel: PartidaViewModel = hiltViewModel(),
    jugadorViewModel: JugadorViewModel = hiltViewModel(),
    onCancel: () -> Unit
) {
    val gameState by partidaViewModel.gameState.collectAsState()
    val jugadoresList by jugadorViewModel.jugadorList.collectAsState(initial = emptyList())

    var showGame by remember { mutableStateOf(false) }
    var showJugadorList by remember { mutableStateOf(false) }
    var selectedJugadorFor by remember { mutableStateOf(1) } // 1 = jugador1, 2 = jugador2

    var jugador1 by remember { mutableStateOf<JugadorEntity?>(null) }
    var jugador2 by remember { mutableStateOf<JugadorEntity?>(null) }
    var partida by remember { mutableStateOf<PartidaEntity?>(null) }

    LaunchedEffect(partidaId) {
        if (partidaId != null) {
            partida = partidaViewModel.getPartidaById(partidaId)
        }
    }

    if (showGame) {
        GameScreen(
            gameState = gameState,
            jugadoresList = jugadoresList,
            onCellClick = partidaViewModel::onCellClick,
            onRestartGame = partidaViewModel::restartGame,
            onBack = {
                showGame = false
            }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Crear Partida",
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

                    // Jugador 1
                    OutlinedTextField(
                        value = jugador1?.Nombres ?: "",
                        onValueChange = {},
                        label = { Text("Jugador 1") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedJugadorFor = 1
                                showJugadorList = true
                            },
                        readOnly = true
                    )

                    // Jugador 2
                    OutlinedTextField(
                        value = jugador2?.Nombres ?: "",
                        onValueChange = {},
                        label = { Text("Jugador 2") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedJugadorFor = 2
                                showJugadorList = true
                            },
                        readOnly = true
                    )

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
                                if (jugador1 != null && jugador2 != null) {
                                    partidaViewModel.startGame(jugador1!!.JugadorId, jugador2!!.JugadorId)
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

    // Diálogo para seleccionar jugador
    if (showJugadorList) {
        Dialog(onDismissRequest = { showJugadorList = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(jugadoresList) { jugador ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedJugadorFor == 1) jugador1 = jugador
                                    else jugador2 = jugador
                                    showJugadorList = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(jugador.Nombres, fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
    jugadoresList: List<JugadorEntity>,
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
                jugadoresList = jugadoresList,
                onCellClick = onCellClick,
                onRestartGame = onRestartGame,
                onExitGame = onBack
            )
        }
    }
}

@Composable
fun GameBoard(
    uiState: GameUiState,
    jugadoresList: List<JugadorEntity>,
    onCellClick: (Int) -> Unit,
    onRestartGame: () -> Unit,
    onExitGame: () -> Unit
) {
    val jugador1Nombre = jugadoresList.find { it.JugadorId == uiState.jugador1Id }?.Nombres ?: "Jugador 1"
    val jugador2Nombre = jugadoresList.find { it.JugadorId == uiState.jugador2Id }?.Nombres ?: "Jugador 2"

    val gameStatus = when {
        uiState.winner != null -> {
            val ganadorNombre = if (uiState.winner == Player.X) jugador1Nombre else jugador2Nombre
            "🏆 ¡Ganador: $ganadorNombre!"
        }
        uiState.isDraw -> "🤝 ¡Es un empate!"
        else -> {
            val turnoNombre = if (uiState.currentPlayer == Player.X) jugador1Nombre else jugador2Nombre
            "Turno de: $turnoNombre"
        }
    }

    Text(
        text = gameStatus,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )

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

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onRestartGame,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Reiniciar Juego", fontSize = 18.sp)
        }

        Button(
            onClick = onExitGame,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Salir del Juego", fontSize = 18.sp)
        }
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
