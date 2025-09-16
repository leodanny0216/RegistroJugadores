package com.example.registrojugadores.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.registrojugadores.data.local.entity.JugadorEntity
import com.example.registrojugadores.data.local.entity.PartidaEntity
import com.example.registrojugadores.presentation.home.DashboardScreen
import com.example.registrojugadores.presentation.jugadores.JugadorListScreen
import com.example.registrojugadores.presentation.jugadores.JugadorScreen
import com.example.registrojugadores.presentation.jugadores.JugadorViewModel
import com.example.registrojugadores.presentation.partida.PartidaListScreen
import com.example.registrojugadores.presentation.partida.PartidaScreen
import com.example.registrojugadores.presentation.partida.PartidaViewModel
import kotlinx.coroutines.launch

@Composable
fun JugadoresNavHost(
    navHostController: NavHostController,
    jugadorViewModel: JugadorViewModel = hiltViewModel(),
    partidaViewModel: PartidaViewModel = hiltViewModel()
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
                    navHostController.navigate("jugador/${jugador.JugadorId}")
                },
                onCreate = {
                    navHostController.navigate("jugador/null")
                },
                onDelete = { jugador ->
                    jugadorViewModel.delete(jugador)
                }
            )
        }

        composable("jugador/{jugadorId}") { backStackEntry ->
            val jugadorIdArg = backStackEntry.arguments?.getString("jugadorId")
            val jugadorId = jugadorIdArg?.toIntOrNull()
            var jugador by remember { mutableStateOf<JugadorEntity?>(null) }
            val scope = rememberCoroutineScope()

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

        composable("partidaList") {
            val partidaList by partidaViewModel.partidas.collectAsState()
            PartidaListScreen(
                viewModel = partidaViewModel,
                onEdit = { partida ->
                    navHostController.navigate("partida/${partida.partidaId}")
                },
                onCreate = {
                    navHostController.navigate("partida/null")
                }
            )
        }

        composable("partida/{partidaId}") { backStackEntry ->
            val partidaIdArg = backStackEntry.arguments?.getString("partidaId")
            val partidaId = partidaIdArg?.toIntOrNull()
            var partida by remember { mutableStateOf<PartidaEntity?>(null) }
            val scope = rememberCoroutineScope()

            LaunchedEffect(partidaId) {
                if (partidaId != null) {
                    partida = partidaViewModel.getPartidaById(partidaId)
                }
            }

            PartidaScreen(
                partida = partida,
                viewModel = partidaViewModel,
                onCancel = { navHostController.popBackStack() }
            )
        }
    }
}