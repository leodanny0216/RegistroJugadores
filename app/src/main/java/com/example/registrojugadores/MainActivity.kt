package com.example.registrojugadores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.registrojugadores.data.local.database.JugadorDb
import com.example.registrojugadores.data.repository.JugadoresRepository
import com.example.registrojugadores.presentation.jugadores.JugadorViewModel
import com.example.registrojugadores.presentation.navegation.JugadoresNavHost
import com.example.registrojugadores.ui.theme.RegistroJugadoresTheme

class MainActivity : ComponentActivity() {
    private lateinit var jugadorDb: JugadorDb
    private lateinit var jugadoresRepository: JugadoresRepository
    private lateinit var jugadorViewModel: JugadorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            JugadorDb::class.java,
            "Jugador.db"
        ).fallbackToDestructiveMigration()
            .build()

        val jugadoresRepository = JugadoresRepository(database.JugadorDao())
        val jugadorViewModel = JugadorViewModel(jugadoresRepository)

        setContent {
            RegistroJugadoresTheme {
                val navController = rememberNavController()
                JugadoresNavHost(
                    navHostController = navController,
                    jugadorViewModel = jugadorViewModel
                )
            }
        }
    }
}