package com.example.registrojugadores.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.registrojugadores.presentation.home.DashboardScreen
import com.example.registrojugadores.presentation.jugadores.JugadorListScreen
import com.example.registrojugadores.presentation.jugadores.JugadorScreen
import com.example.registrojugadores.presentation.jugadores.JugadorViewModel

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
            val jugador = jugadorViewModel.getJugadorById(jugadorId)

            JugadorScreen(
                jugador = jugador,
                agregarJugador = { nombres, partidas ->
                    if (jugador == null) {
                        jugadorViewModel. agregarJugador(nombres, partidas)
                    } else {
                        jugadorViewModel.update(jugador.copy(Nombres = nombres, Partidas = partidas))
                    }
                    navHostController.popBackStack()
                },
                onCancel = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}