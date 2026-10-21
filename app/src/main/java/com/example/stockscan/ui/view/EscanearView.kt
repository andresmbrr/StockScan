package com.example.stockscan.ui.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.Perfil
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun EscanearView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var permiso by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    var habilitarCamara by remember { mutableStateOf(false) }
    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        permiso = it
        habilitarCamara = it
    }
    Pantalla(navController, viewModel, "Escanear producto") {
        Text("Usa la cámara para leer un código de barras o ingrésalo manualmente.")
        if (permiso && habilitarCamara) {
            CamaraCodigoBarras(onCodigo = { codigo ->
                habilitarCamara = false
                if (e.perfil == Perfil.OPERADOR && e.conteo != null && e.conteo.enRevision == false) {
                    viewModel.leerEnConteo(codigo)
                    navController.irA(Destinos.Conteo.ruta)
                } else if (viewModel.codigoEscaneado(codigo)) {
                    navController.irA(Destinos.Detalle.ruta)
                }
            })
            OutlinedButton(onClick = { habilitarCamara = false }, modifier = Modifier.fillMaxWidth()) { Text("Cerrar cámara") }
        } else {
            Accion("Abrir cámara", {
                if (permiso) habilitarCamara = true else pedirPermiso.launch(Manifest.permission.CAMERA)
            })
            if (!permiso) Text("Si denegaste el permiso, habilítalo en Ajustes o usa el ingreso manual.")
        }
        Seccion("O ingresa el código manualmente")
        OutlinedTextField(value = e.codigoManual, onValueChange = viewModel::onCodigoManualChange,
            modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Código de barras") })
        Accion("Consultar código", { if (viewModel.buscarCodigo()) navController.irA(Destinos.Detalle.ruta) })
        Text("Códigos de prueba: 2000000000015 · 2000000000022 · 2000000000039")
        Text("La cámara solo procesa los códigos; no se guardan fotografías ni videos.")
    }
}
