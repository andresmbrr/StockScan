package com.example.stockscan.ui.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun ResumenConteoView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    val c = e.conteo
    Pantalla(navController, viewModel, "Resumen del conteo") {
        if (c == null || !c.enRevision) { Text("No hay conteo pendiente de confirmar"); return@Pantalla }
        Seccion("C-${c.id} · ${e.diferenciasConteo().size} con diferencias")
        Text("Tiempo total: ${(System.currentTimeMillis() - c.inicioMillis) / 1000} segundos")
        e.productos.forEach { p ->
            val anterior = c.stockInicial[p.codigoInterno] ?: 0
            val contado = c.contados[p.codigoInterno] ?: 0
            Tarjeta(p.nombre, "Registrado: $anterior · Contado: $contado · Diferencia: ${contado - anterior} u.")
        }
        Text("Confirmar fijará el stock contado y generará solo los movimientos con diferencia.")
        Accion("Confirmar ${e.diferenciasConteo().size} diferencias", {
            if (viewModel.confirmarConteo()) navController.irA(Destinos.Inicio.ruta)
        })
        Accion("Volver al conteo", { navController.irA(Destinos.Conteo.ruta) })
    }
}
