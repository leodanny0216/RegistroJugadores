package com.example.registrojugadores.presentation.jugadores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrojugadores.data.local.entity.JugadorEntity

@Composable
fun JugadorListScreen(
    jugadorList: List<JugadorEntity>,
    onCreate: () -> Unit,
    onDelete: (JugadorEntity) -> Unit,
    onEdit: (JugadorEntity) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Jugador")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF0D47A1)) // Azul oscuro
                    )
                )

                .padding(paddingValues)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Lista de Jugadores",
                style = TextStyle(
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                items(jugadorList) { jugador ->
                    JugadorRow(jugador, onDelete, onEdit)
                }
            }
        }
    }
}

@Composable
fun JugadorRow(
    jugador: JugadorEntity,
    onDelete: (JugadorEntity) -> Unit,
    onEdit: (JugadorEntity) -> Unit
) {
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
                Row {
                    Text("Nombre: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(jugador.Nombres, fontSize = 16.sp)
                }
                Row {
                    Text("Partidas: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(jugador.Partidas.toString(), fontSize = 16.sp)
                }
            }

            Row {
                IconButton(onClick = { onEdit(jugador) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color(0xFF4CAF50))
                }
                IconButton(onClick = { onDelete(jugador) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}