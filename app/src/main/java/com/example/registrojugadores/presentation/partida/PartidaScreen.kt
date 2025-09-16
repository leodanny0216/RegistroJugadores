package com.example.registrojugadores.presentation.partida

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    var fecha by remember { mutableStateOf(partida?.fecha ?: Date()) }
    var jugador1Id by remember { mutableStateOf(partida?.jugador1Id?.toString() ?: "") }
    var jugador2Id by remember { mutableStateOf(partida?.jugador2Id?.toString() ?: "") }
    var ganadorId by remember { mutableStateOf(partida?.ganadorId?.toString() ?: "") }
    var esFinalizada by remember { mutableStateOf(partida?.esFinalizada ?: false) }

    var errorJugador1 by remember { mutableStateOf<String?>(null) }
    var errorJugador2 by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (partida == null) "Registrar Partida" else "Editar Partida",
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
                )
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
                OutlinedTextField(
                    value = jugador1Id,
                    onValueChange = { jugador1Id = it },
                    label = { Text("Jugador 1 Id") },
                    isError = errorJugador1 != null,
                    modifier = Modifier.fillMaxWidth()
                )
                errorJugador1?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }

                OutlinedTextField(
                    value = jugador2Id,
                    onValueChange = { jugador2Id = it },
                    label = { Text("Jugador 2 Id") },
                    isError = errorJugador2 != null,
                    modifier = Modifier.fillMaxWidth()
                )
                errorJugador2?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }

                OutlinedTextField(
                    value = ganadorId,
                    onValueChange = { ganadorId = it },
                    label = { Text("Ganador Id (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = esFinalizada,
                        onCheckedChange = { esFinalizada = it }
                    )
                    Text("Finalizada", fontSize = 16.sp)
                }

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
                            val gId = ganadorId.toIntOrNull()

                            if (j1 == null) errorJugador1 = "Jugador 1 Id requerido"
                            else errorJugador1 = null

                            if (j2 == null) errorJugador2 = "Jugador 2 Id requerido"
                            else errorJugador2 = null

                            if (j1 != null && j2 != null) {
                                viewModel.savePartida(fecha, j1, j2, gId, esFinalizada, partida?.partidaId)
                                onCancel()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar", tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
