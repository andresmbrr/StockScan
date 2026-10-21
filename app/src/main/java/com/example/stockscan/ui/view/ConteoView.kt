package com.example.stockscan.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun ConteoView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    val c = e.conteo
    var codigo by remember { mutableStateOf("") }
    var transcurrido by remember(c?.id) { mutableLongStateOf(0L) }
    LaunchedEffect(c?.id) {
        while (c != null) { transcurrido = (System.currentTimeMillis() - c.inicioMillis) / 1000; delay(1000) }
    }
    Pantalla(navController, viewModel, "Conteo físico") {
        if (c == null) { Text("No hay conteo en curso"); Accion("Iniciar conteo", { viewModel.iniciarConteo() }); return@Pantalla }
        Seccion("Sesión C-${c.id} · ${c.contados.size} de ${e.productos.size} productos")
        Text("Tiempo transcurrido: ${transcurrido / 60} min ${transcurrido % 60} s")
        Accion("Leer con la cámara", { navController.irA(Destinos.Escanear.ruta) })
        Text("Escaneos repetidos agregan +1. Puedes fijar una cantidad manualmente.")
        OutlinedTextField(value = codigo, onValueChange = { codigo = it.filter(Char::isDigit) },
            label = { Text("Código leído / ingreso manual") }, modifier = Modifier.fillMaxWidth())
        Accion("Agregar +1 por código", { if (viewModel.leerEnConteo(codigo)) codigo = "" })
        e.productos.forEach { p ->
            Text("${p.nombre} · ${p.codigoInterno} · en servidor demo ${c.stockInicial[p.codigoInterno]} u.")
            OutlinedTextField(value = c.contados[p.codigoInterno]?.toString() ?: "",
                onValueChange = { if(it.isEmpty()) viewModel.limpiarCantidadConteo(p.codigoInterno) else viewModel.fijarCantidad(p.codigoInterno, it) },
                label = { Text("Cantidad contada") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            TextButton(onClick = { viewModel.leerEnConteo(p.codigoBarras) }) { Text("+1 unidad contada") }
            HorizontalDivider()
        }
        Text("El stock no cambia hasta confirmar el resumen.")
        Accion("Cerrar y revisar conteo", { if(viewModel.revisarConteo()) navController.irA(Destinos.ResumenConteo.ruta) })
        OutlinedButton(onClick = { viewModel.cancelarConteo(); navController.irA(Destinos.Inicio.ruta) },
            modifier = Modifier.fillMaxWidth()) { Text("Cancelar conteo (sin guardar)") }
    }
}
