package com.example.registrojugadores.presentation.partida

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.registrojugadores.data.local.entity.JugadorEntity
import com.example.registrojugadores.data.local.entity.PartidaEntity
import com.example.registrojugadores.presentation.jugadores.JugadorViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PartidaListScreen(
    partidaViewModel: PartidaViewModel = hiltViewModel(),
    jugadorViewModel: JugadorViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (PartidaEntity) -> Unit
) {
    val partidas = partidaViewModel.partidas.collectAsState()
    val jugadoresList by jugadorViewModel.jugadorList.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Partida")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF0D47A1))
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Lista de Partidas",
                style = TextStyle(
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                items(partidas.value) { partida ->
                    PartidaRow(
                        partida = partida,
                        jugadoresList = jugadoresList,
                        onDelete = { partidaViewModel.deletePartida(it) },
                        onEdit = onEdit
                    )
                }
            }
        }
    }
}
@Composable
fun PartidaRow(
    partida: PartidaEntity,
    jugadoresList: List<JugadorEntity>,
    onDelete: (PartidaEntity) -> Unit,
    onEdit: (PartidaEntity) -> Unit
) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val jugador1Nombre = jugadoresList.find { it.JugadorId == partida.jugador1Id }?.Nombres ?: "Jugador 1"
    val jugador2Nombre = jugadoresList.find { it.JugadorId == partida.jugador2Id }?.Nombres ?: "Jugador 2"
    val ganadorNombre = jugadoresList.find { it.JugadorId == partida.ganadorId }?.Nombres ?: "N/A"

    Card(
        elevation = CardDefaults.cardElevation(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(22.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Fecha: ${formatter.format(partida.fecha)}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Jugador 1: $jugador1Nombre", fontSize = 16.sp)
                Text("Jugador 2: $jugador2Nombre", fontSize = 16.sp)
                Text("Ganador: $ganadorNombre", fontSize = 16.sp)
                Text("Finalizada: ${if (partida.esFinalizada) "Sí" else "No"}", fontSize = 16.sp)
            }

            Row {
                IconButton(onClick = { onEdit(partida) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color(0xFF4CAF50))
                }

                IconButton(onClick = { onDelete(partida) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}
