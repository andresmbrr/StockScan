package com.example.stockscan.ui.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.ExportarCsv
import com.example.stockscan.model.Perfil
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun ReportesView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var tipo by remember { mutableStateOf("Stock") }
    var mensajeLocal by remember { mutableStateOf("") }
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
        if (uri != null) {
            try {
                val salida = context.contentResolver.openOutputStream(uri)
                    ?: error("No se pudo abrir la ubicación de destino")
                salida.use { archivo ->
                    archivo.write((if (tipo == "Stock") ExportarCsv.stock(e) else ExportarCsv.historial(e)).toByteArray(Charsets.UTF_8))
                }
                mensajeLocal = "Reporte CSV guardado en el lugar elegido"
            } catch(ex: Exception) { mensajeLocal = "No se pudo guardar: ${ex.message}" }
        }
    }
    Pantalla(navController, viewModel, "Exportar reportes") {
        if (e.perfil != Perfil.GERENCIAL) { Text("Acceso exclusivo de gerencia"); return@Pantalla }
        Seccion("Elige el reporte")
        RowFiltros(listOf("Stock", "Historial"), tipo) { tipo = it }
        Tarjeta("Exportación local · CSV", "${e.tiendas.count { it.habilitada }} tiendas · ${e.productos.size} productos · ${e.movimientos.size} movimientos")
        Accion("Guardar archivo CSV real", { lanzador.launch(if(tipo == "Stock") "stock_consolidado_demo.csv" else "historial_demo.csv") })
        OutlinedButton(onClick = { mensajeLocal = "Exportación XLSX simulada (el mockup no genera un archivo real)." }) {
            Text("Simular reporte Excel .xlsx")
        }
        if (mensajeLocal.isNotBlank()) Text(mensajeLocal)
        Text("Los datos exportados son sintéticos. No se envían a terceros.")
    }
}
