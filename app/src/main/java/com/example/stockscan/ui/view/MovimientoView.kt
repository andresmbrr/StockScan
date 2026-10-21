package com.example.stockscan.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.Perfil
import com.example.stockscan.model.TipoMovimiento
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun MovimientoView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    var confirmar by remember { mutableStateOf(false) }
    Pantalla(navController, viewModel, "Registrar movimiento") {
        val p = e.productoActual()
        if (p == null || e.perfil != Perfil.OPERADOR) { Text("No tienes permiso o falta producto"); return@Pantalla }
        Seccion(p.nombre)
        Text("${p.codigoInterno} · Stock actual: ${e.stockDe(p)} u.")
        Seccion("Tipo de movimiento")
        TipoMovimiento.entries.filter { it != TipoMovimiento.CONTEO }.forEach { tipo ->
            Row(modifier = Modifier.fillMaxWidth()) {
                RadioButton(selected = e.movimientoTipo == tipo, onClick = { viewModel.onTipoMovimiento(tipo) })
                TextButton(onClick = { viewModel.onTipoMovimiento(tipo) }) { Text(tipo.titulo) }
            }
        }
        OutlinedTextField(value = e.movimientoCantidad, onValueChange = viewModel::onCantidadMovimiento,
            label = { Text(if (e.movimientoTipo == TipoMovimiento.AJUSTE) "Cambio de unidades (+/-)" else "Cantidad de unidades") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        if (e.movimientoTipo == TipoMovimiento.AJUSTE) {
            OutlinedTextField(value = e.movimientoMotivo, onValueChange = viewModel::onMotivoMovimiento,
                label = { Text("Motivo obligatorio") }, modifier = Modifier.fillMaxWidth())
        } else {
            OutlinedTextField(value = e.movimientoMotivo, onValueChange = viewModel::onMotivoMovimiento,
                label = { Text("Referencia opcional") }, modifier = Modifier.fillMaxWidth())
        }
        Text("Actual: ${e.stockDe(p)} u. → Resultante: ${viewModel.resultadoMovimiento()?.let { "$it u." } ?: "Cantidad inválida"}")
        Accion("Revisar y confirmar movimiento", { confirmar = true }, e.movimientoCantidad.isNotBlank())
    }
    if (confirmar) AlertDialog(onDismissRequest = { confirmar = false }, title = { Text("Confirmar movimiento") },
        text = { Text("${e.movimientoTipo.titulo}: ${e.movimientoCantidad} · resultante ${viewModel.resultadoMovimiento() ?: "inválido"} u.\nSolo se guarda en la demo local.") },
        confirmButton = { TextButton(onClick = {
            confirmar = false
            if (viewModel.registrarMovimiento()) navController.irA(Destinos.Detalle.ruta)
        }) { Text("Confirmar") } },
        dismissButton = { TextButton(onClick = { confirmar = false }) { Text("Cancelar") } })
}
