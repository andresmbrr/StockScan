package com.example.stockscan.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun ProductosView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    Pantalla(navController, viewModel, "Productos y stock") {
        OutlinedTextField(value = e.busqueda, onValueChange = viewModel::onBusquedaChange,
            label = { Text("Buscar por nombre o código") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Text("${e.productosFiltrados().size} referencias · ${e.tiendaActual()?.nombre}")
        e.productosFiltrados().forEach { p ->
            val cantidad = e.stockDe(p)
            Tarjeta(p.nombre, "${p.codigoInterno} · ${p.categoria}\n${p.codigoBarras}\n$cantidad u. · mín. ${p.stockMinimo} · " +
                if (cantidad <= p.stockMinimo) "Stock mínimo" else "Sobre mínimo") {
                viewModel.seleccionarProducto(p.codigoInterno)
                navController.irA(Destinos.Detalle.ruta)
            }
        }
        if (e.productosFiltrados().isEmpty()) Text("Producto no encontrado")
    }
}
