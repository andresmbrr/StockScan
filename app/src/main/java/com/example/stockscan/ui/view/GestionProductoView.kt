package com.example.stockscan.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.Perfil
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun GestionProductoView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    val p = e.productoActual()
    var nombre by remember(e.productoId) { mutableStateOf(p?.nombre ?: "") }
    var barras by remember(e.productoId) { mutableStateOf(p?.codigoBarras ?: "") }
    var categoria by remember(e.productoId) { mutableStateOf(p?.categoria ?: "") }
    var descripcion by remember(e.productoId) { mutableStateOf(p?.descripcion ?: "") }
    var minimo by remember(e.productoId) { mutableStateOf(p?.stockMinimo?.toString() ?: "0") }
    Pantalla(navController, viewModel, if(p == null) "Crear producto" else "Editar · ${p.codigoInterno}") {
        if(e.perfil != Perfil.GERENCIAL) { Text("Acceso exclusivo de gerencia"); return@Pantalla }
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = barras, onValueChange = { barras = it.filter(Char::isDigit) }, label = { Text("Código de barras") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = minimo, onValueChange = { minimo = it.filter(Char::isDigit) }, label = { Text("Stock mínimo") }, modifier = Modifier.fillMaxWidth())
        if(p != null) e.tiendas.forEach { t -> Text("${t.nombre}: ${e.stockDe(p, t.id)} u. (solo lectura)") }
        Text("Editar el catálogo no modifica las existencias de las tiendas.")
        Accion("Guardar producto", {
            if (viewModel.guardarProducto(p?.codigoInterno ?: "", nombre, barras, categoria, descripcion, minimo)) {
                navController.irA(Destinos.Gestion.ruta)
            }
        })
    }
}
