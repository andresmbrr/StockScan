package com.example.stockscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.ui.theme.StockScanTheme
import com.example.stockscan.ui.view.*
import com.example.stockscan.viewmodel.StockScanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = StockScanViewModel()
        setContent {
            StockScanTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Destinos.Login.ruta) {
                        composable(Destinos.Login.ruta) { LoginView(navController, viewModel) }
                        composable(Destinos.Tiendas.ruta) { TiendasView(navController, viewModel) }
                        composable(Destinos.Inicio.ruta) { InicioView(navController, viewModel) }
                        composable(Destinos.Escanear.ruta) { EscanearView(navController, viewModel) }
                        composable(Destinos.Productos.ruta) { ProductosView(navController, viewModel) }
                        composable(Destinos.Detalle.ruta) { DetalleView(navController, viewModel) }
                        composable(Destinos.Movimiento.ruta) { MovimientoView(navController, viewModel) }
                        composable(Destinos.Conteo.ruta) { ConteoView(navController, viewModel) }
                        composable(Destinos.ResumenConteo.ruta) { ResumenConteoView(navController, viewModel) }
                        composable(Destinos.Alertas.ruta) { AlertasView(navController, viewModel) }
                        composable(Destinos.Historial.ruta) { HistorialView(navController, viewModel) }
                        composable(Destinos.Resumen.ruta) { ResumenView(navController, viewModel) }
                        composable(Destinos.Gestion.ruta) { GestionView(navController, viewModel) }
                        composable(Destinos.GestionProducto.ruta) { GestionProductoView(navController, viewModel) }
                        composable(Destinos.Reportes.ruta) { ReportesView(navController, viewModel) }
                    }
                }
            }
        }
    }
}
