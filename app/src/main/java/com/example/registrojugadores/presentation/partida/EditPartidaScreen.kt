package com.example.registrojugadores.presentation.partida
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
fun EditPartidaScreen(
    navController: NavController,
    partidaId: Int,
    partidaViewModel: PartidaViewModel = hiltViewModel(),
    jugadorViewModel: JugadorViewModel = hiltViewModel(),
    onCancel: () -> Unit
) {
    val jugadoresList by jugadorViewModel.jugadorList.collectAsState(initial = emptyList())
    var partida by remember { mutableStateOf<PartidaEntity?>(null) }
    var jugador1 by remember { mutableStateOf<JugadorEntity?>(null) }
    var jugador2 by remember { mutableStateOf<JugadorEntity?>(null) }
    var showJugadorList by remember { mutableStateOf(false) }
    var selectedJugadorFor by remember { mutableStateOf(1) }
    var esFinalizada by remember { mutableStateOf(false) }

    LaunchedEffect(partidaId, jugadoresList) {
        val p = partidaViewModel.getPartidaById(partidaId)
        partida = p
        esFinalizada = p?.esFinalizada ?: false
        jugador1 = jugadoresList.find { it.JugadorId == p?.jugador1Id }
        jugador2 = jugadoresList.find { it.JugadorId == p?.jugador2Id }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Partida") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF0D47A1))
                    )
                )
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = esFinalizada,
                    onCheckedChange = { esFinalizada = it }
                )
                Text("Partida Finalizada")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (jugador1 != null && jugador2 != null && partida != null) {

                            partidaViewModel.savePartida(
                                fecha = partida!!.fecha,
                                jugador1Id = jugador1!!.JugadorId!!,
                                jugador2Id = jugador2!!.JugadorId!!,
                                ganadorId = partida!!.ganadorId,
                                esFinalizada = esFinalizada,
                                id = partida!!.partidaId
                            )
                            onCancel()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Guardar", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Guardar Cambios")
                }
            }
        }
    }

    if (showJugadorList) {
        Dialog(onDismissRequest = { showJugadorList = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
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