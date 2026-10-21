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
fun DetalleView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    val p = e.productoActual()
    Pantalla(navController, viewModel, "Detalle de producto") {
        if (p == null) { Text("Producto no encontrado") }
        else {
            val cantidad = e.stockDe(p)
            Seccion(p.nombre)
            Text("${p.categoria} · ${p.codigoInterno}")
            Tarjeta("$cantidad u. disponibles", "Stock mínimo: ${p.stockMinimo} u. · " +
                when { cantidad == 0 -> "En quiebre"; cantidad <= p.stockMinimo -> "Stock bajo"; else -> "Stock suficiente" })
            Text("Código de barras: ${p.codigoBarras}")
            Text(p.descripcion)
            Text("Stock mostrado para ${e.tiendaActual()?.nombre} (DEMO).")
            if (e.perfil == Perfil.OPERADOR) {
                Accion("Registrar movimiento", { navController.irA(Destinos.Movimiento.ruta) })
            }
            Accion("Volver a productos", { navController.irA(Destinos.Productos.ruta) })
        }
    }
}
