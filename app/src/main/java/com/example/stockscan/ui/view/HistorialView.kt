package com.example.stockscan.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun HistorialView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    Pantalla(navController, viewModel, "Historial") {
        Text("Registros inmutables en memoria. Corregir implica generar un nuevo ajuste con motivo.")
        OutlinedTextField(value = e.filtroHistorial, onValueChange = viewModel::onFiltroHistorial,
            label = { Text("Filtrar por código, usuario o tipo") }, modifier = Modifier.fillMaxWidth())
        val lista = e.movimientos.filter {
            (e.perfil == com.example.stockscan.model.Perfil.GERENCIAL || it.tiendaId == e.tiendaId) &&
                (it.productoId.contains(e.filtroHistorial, ignoreCase = true) ||
                    it.usuario.contains(e.filtroHistorial, ignoreCase = true) ||
                    it.tipo.titulo.contains(e.filtroHistorial, ignoreCase = true))
        }
        Seccion("${lista.size} movimientos")
        if(lista.isEmpty()) Text("Sin movimientos que mostrar. Registra una entrada o confirma un conteo de prueba.")
        lista.forEach { m ->
            val producto = e.productos.find { it.codigoInterno == m.productoId }
            Tarjeta("${producto?.nombre ?: m.productoId} · ${if(m.cantidad > 0) "+" else ""}${m.cantidad} u.",
                "${m.tipo.titulo} · ${m.anterior} → ${m.nuevo} u.\n${m.tiendaId} · ${m.usuario}\n${m.fechaUtc} UTC\n${m.motivo}")
        }
    }
}
