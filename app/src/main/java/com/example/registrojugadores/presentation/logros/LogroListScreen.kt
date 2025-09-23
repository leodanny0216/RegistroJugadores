package com.example.registrojugadores.presentation.logros

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrojugadores.data.local.entity.JugadorEntity
import com.example.registrojugadores.data.local.entity.LogroEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LogroListScreen(
    logroList: List<LogroEntity>,
    jugadores: List<JugadorEntity>,
    onCreate: () -> Unit,
    onDelete: (LogroEntity) -> Unit,
    onEdit: (LogroEntity) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFF1565C0))
                    )
                )
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de Logros",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF0D47A1),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(logroList) { logro ->
                    LogroRow(logro, jugadores, onDelete, onEdit)
                }
            }
        }
    }
}

@Composable
fun LogroRow(
    logro: LogroEntity,
    jugadores: List<JugadorEntity>,
    onDelete: (LogroEntity) -> Unit,
    onEdit: (LogroEntity) -> Unit
) {
    val jugadorNombre = jugadores.find { it.JugadorId == logro.jugadorId }?.Nombres ?: "Desconocido"

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Jugador: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(jugadorNombre, fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Descripción: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(logro.descripcion, fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Partida ID: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${logro.partidaId ?: "N/A"}", fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Fecha: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        logro.fecha?.let {
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
                        } ?: "N/A",
                        fontSize = 16.sp
                    )
                }
            }

            Row {
                IconButton(onClick = { onEdit(logro) }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF1976D2)
                    )
                }
                IconButton(onClick = { onDelete(logro) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}