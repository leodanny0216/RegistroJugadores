package com.example.registrojugadores.presentation.jugadores

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
import com.example.registrojugadores.data.local.entity.JugadorEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorScreen(
    jugador: JugadorEntity?,
    onSaveJugador: (String, Int) -> Unit,
    onCancel: () -> Unit
) {
    var nombres by remember { mutableStateOf(jugador?.Nombres ?: "") }
    var partidas by remember { mutableStateOf(jugador?.Partidas?.toString() ?: "") }
    var nombreError by remember { mutableStateOf<String?>(null) }
    var partidasError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (jugador == null) "Registrar Jugador" else "Editar Jugador",
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
                    value = nombres,
                    onValueChange = {
                        nombres = it
                        if (it.isNotBlank()) nombreError = null
                    },
                    label = { Text("Nombre del Jugador") },
                    isError = nombreError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                nombreError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = partidas,
                    onValueChange = {
                        partidas = it
                        if (it.toIntOrNull() != null) partidasError = null
                    },
                    label = { Text("Partidas") },
                    isError = partidasError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                partidasError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onCancel() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            var valid = true

                            if (nombres.isBlank()) {
                                nombreError = "El nombre es requerido"
                                valid = false
                            }
                            val partidasInt = partidas.toIntOrNull()
                            if (partidas.isBlank()) {
                                partidasError = "Las partidas son requeridas"
                                valid = false
                            } else if (partidasInt == null) {
                                partidasError = "Debe ser un número válido"
                                valid = false
                            }

                            if (valid) {
                                onSaveJugador(nombres, partidasInt!!)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Guardar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
