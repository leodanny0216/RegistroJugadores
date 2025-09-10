package com.example.registrojugadores.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.registrojugadores.data.local.entity.JugadorEntity
import com.example.registrojugadores.presentation.home.DashboardScreen
import com.example.registrojugadores.presentation.jugadores.JugadorListScreen
import com.example.registrojugadores.presentation.jugadores.JugadorScreen
import com.example.registrojugadores.presentation.jugadores.JugadorViewModel
import kotlinx.coroutines.launch

@Composable
fun JugadoresNavHost(
    navHostController: NavHostController,
    jugadorViewModel: JugadorViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = "dashboard"
    ) {

        composable("dashboard") {
            DashboardScreen(navController = navHostController)
        }

        composable("jugadorList") {
            val jugadorList = jugadorViewModel.jugadorList.collectAsState().value

            JugadorListScreen(
                jugadorList = jugadorList,
                onEdit = { jugador ->
                    navHostController.navigate(Screen.Jugador(jugador.JugadorId))
                },
                onCreate = {
                    navHostController.navigate(Screen.Jugador(null))
                },
                onDelete = { jugador ->
                    jugadorViewModel.delete(jugador)
                }
            )
        }

        composable<Screen.Jugador> { backStackEntry ->
            val jugadorId = backStackEntry.toRoute<Screen.Jugador>().jugadorId
            val scope = rememberCoroutineScope()
            var jugador by remember { mutableStateOf<JugadorEntity?>(null) }

            LaunchedEffect(jugadorId) {
                if (jugadorId != null) {
                    jugador = jugadorViewModel.getJugadorById(jugadorId)
                }
            }

            JugadorScreen(
                jugador = jugador,
                onSaveJugador = { nombres, partidas ->
                    scope.launch {
                        jugadorViewModel.saveJugador(
                            nombres = nombres,
                            partidas = partidas,
                            id = jugador?.JugadorId
                        )
                        navHostController.popBackStack()
                    }
                },
                onCancel = {
                    navHostController.popBackStack()
                }
            )
        }

    }
}