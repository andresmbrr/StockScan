package com.example.stockscan.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.Perfil
import com.example.stockscan.model.TipoAlerta
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun AlertasView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    var productoId by remember { mutableStateOf("P-001") }
    var motivo by remember { mutableStateOf("") }
    var filtro by remember { mutableStateOf("Todas") }
    Pantalla(navController, viewModel, "Alertas de bodega") {
        if(e.perfil != Perfil.BODEGA) { Text("Solo el encargado de bodega puede gestionar alertas."); return@Pantalla }
        val lista = (e.alertas + e.alertasMinimo()).filter { it.tiendaId == e.tiendaId }
        Text("${lista.size} alertas · ${lista.count { !it.resuelta }} activas")
        RowFiltros(listOf("Todas", "Activas", "Resueltas"), filtro) { filtro = it }
        lista.filter { when(filtro) { "Activas" -> !it.resuelta; "Resueltas" -> it.resuelta; else -> true } }.forEach { a ->
            val producto = e.productos.find { it.codigoInterno == a.productoId }
            Tarjeta("${a.tipo.titulo} · ${producto?.nombre ?: a.productoId}",
                "${a.descripcion}\n${if(a.resuelta) "Resuelta" else "Activa"} · ${a.usuario} · ${a.fechaUtc}")
            if(!a.resuelta && a.tipo != TipoAlerta.MINIMO) {
                TextButton(onClick = { viewModel.resolverAlerta(a.id) }) { Text("Resolver alerta") }
            }
        }
        Seccion("Declarar reposición")
        e.productos.forEach { p ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(selected = productoId == p.codigoInterno, onClick = { productoId = p.codigoInterno })
                Text(p.nombre)
            }
        }
        OutlinedTextField(value = motivo, onValueChange = { motivo = it }, label = { Text("Motivo obligatorio") },
            modifier = Modifier.fillMaxWidth())
        Accion("Declarar reposición", { if (viewModel.declararReposicion(productoId, motivo)) motivo = "" })
        Text("Declarar una alerta no aumenta el stock.")
    }
}

@Composable
fun RowFiltros(opciones: List<String>, seleccionado: String, cambio: (String) -> Unit) {
    androidx.compose.foundation.layout.Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
        opciones.forEach { opcion ->
            FilterChip(selected = seleccionado == opcion, onClick = { cambio(opcion) }, label = { Text(opcion) })
        }
    }
}
