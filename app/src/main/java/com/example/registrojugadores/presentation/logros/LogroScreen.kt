package com.example.registrojugadores.presentation.logros

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun LogroScreen(
    viewModel: LogroViewModel,
    logro: LogroEntity?,
    jugadores: List<JugadorEntity>,
    onCancel: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    var jugadorSeleccionado by remember {
        mutableStateOf(jugadores.find { it.JugadorId == logro?.jugadorId }?.Nombres ?: "")
    }
    var descripcion by remember { mutableStateOf(logro?.descripcion ?: "") }
    var partidaId by remember { mutableStateOf(logro?.partidaId?.toString() ?: "") }
    var fecha by remember { mutableStateOf(logro?.fecha?.let { dateFormat.format(it) } ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFEFB8C8), Color(0xFFEFB8C8))
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = if (logro == null) "Registrar Logro" else "Editar Logro",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))
        Box {
            OutlinedTextField(
                value = jugadorSeleccionado,
                onValueChange = { jugadorSeleccionado = it },
                label = { Text("Jugador", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDropdown = true },
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                jugadores.forEach { jugador ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                jugador.Nombres,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        onClick = {
                            jugadorSeleccionado = jugador.Nombres
                            showDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = partidaId,
            onValueChange = { partidaId = it },
            label = { Text("ID de la partida (opcional)", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = { },
            label = { Text("Fecha", color = Color.Black) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    modifier = Modifier.clickable { showDatePicker = true }
                )
            },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )


        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                onDateSelected = { selectedDate ->
                    fecha = dateFormat.format(selectedDate)
                    showDatePicker = false
                },
                onDismissRequest = { showDatePicker = false },
                initialDate = logro?.fecha ?: calendar.time
            )
        }

        errorMessage?.let {
            Text(it, color = Color.Red)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    val jugador = jugadores.find { it.Nombres == jugadorSeleccionado }
                    val partidaIdInt = partidaId.toIntOrNull()
                    val fechaDate = try { dateFormat.parse(fecha) } catch (e: Exception) { null }

                    val result = viewModel.validarYAgregar(
                        logro?.logroId,
                        jugador?.JugadorId,
                        descripcion,
                        fechaDate,
                        partidaIdInt
                    )

                    if (result.isValid) {
                        onCancel()
                    } else {
                        errorMessage = result.errorMessage
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text(if (logro == null) "Guardar" else "Actualizar")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (Date) -> Unit,
    onDismissRequest: () -> Unit,
    initialDate: Date
) {
    val calendar = Calendar.getInstance().apply { time = initialDate }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)
                onDateSelected(selectedCalendar.time)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(
            state = rememberDatePickerState(initialSelectedDateMillis = initialDate.time)
        )
    }
}