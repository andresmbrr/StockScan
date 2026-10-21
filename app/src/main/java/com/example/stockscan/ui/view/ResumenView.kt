package com.example.stockscan.ui.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.Perfil
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun ResumenView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    Pantalla(navController, viewModel, "Resumen global") {
        if (e.perfil != Perfil.GERENCIAL) { Text("Acceso exclusivo de gerencia"); return@Pantalla }
        Seccion("Todas las tiendas habilitadas")
        val habilitadas = e.tiendas.filter { it.habilitada }
        val total = habilitadas.sumOf { e.totalUnidades(it.id) }
        Tarjeta("$total unidades totales", "${e.productos.size} productos únicos · ${habilitadas.size} tiendas habilitadas")
        Tarjeta("${e.alertasActivas().size} alertas activas", "${e.alertasMinimo().size} avisos automáticos por stock mínimo")
        Seccion("Stock por tienda")
        habilitadas.forEach { t -> Tarjeta(t.nombre, "${e.totalUnidades(t.id)} unidades · ${t.id}") }
        Text("Tiendas deshabilitadas excluidas del consolidado. Datos sintéticos locales, no servidor.")
        Accion("Administrar tiendas y productos", { navController.irA(Destinos.Gestion.ruta) })
        Accion("Historial de movimientos", { navController.irA(Destinos.Historial.ruta) })
        Accion("Exportar reportes", { navController.irA(Destinos.Reportes.ruta) })
    }
}
