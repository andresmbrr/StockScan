package com.example.stockscan.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stockscan.model.Perfil
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

fun NavController.irA(ruta: String) { navigate(ruta) { launchSingleTop = true } }

@Composable
fun Pantalla(navController: NavController, viewModel: StockScanViewModel, titulo: String,
            contenido: @Composable ColumnScope.() -> Unit) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    val rutaActual = navController.currentBackStackEntryAsState().value?.destination?.route
    val pestañas = when(e.perfil) {
        Perfil.OPERADOR -> listOf(Triple("Inicio", "⌂", Destinos.Inicio.ruta), Triple("Escanear", "▣", Destinos.Escanear.ruta),
            Triple("Productos", "▤", Destinos.Productos.ruta), Triple("Historial", "◷", Destinos.Historial.ruta))
        Perfil.BODEGA -> listOf(Triple("Inicio", "⌂", Destinos.Inicio.ruta), Triple("Consultar", "▣", Destinos.Escanear.ruta),
            Triple("Alertas", "!", Destinos.Alertas.ruta))
        Perfil.GERENCIAL -> listOf(Triple("Resumen", "▥", Destinos.Resumen.ruta), Triple("Gestión", "⚙", Destinos.Gestion.ruta),
            Triple("Historial", "◷", Destinos.Historial.ruta), Triple("Reportes", "⇩", Destinos.Reportes.ruta))
    }
    Scaffold(bottomBar = {
        NavigationBar {
            pestañas.forEach { (nombre, icono, ruta) ->
                NavigationBarItem(selected = rutaActual == ruta, onClick = { navController.irA(ruta) },
                    icon = { Text(icono, fontSize = 19.sp) }, label = { Text(nombre, fontSize = 10.sp) })
            }
        }
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(titulo, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("${e.perfil.descripcion} · ${e.usuario}", fontSize = 12.sp)
                }
                TextButton(onClick = { navController.irA(Destinos.Tiendas.ruta) }) { Text(e.tiendaId ?: "Tienda") }
            }
            HorizontalDivider()
            Text("DEMO académica · datos ficticios en memoria · sin API REST", fontSize = 11.sp,
                color = MaterialTheme.colorScheme.secondary)
            if(e.mensaje != null) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(e.mensaje!!, modifier = Modifier.weight(1f), fontSize = 13.sp)
                        TextButton(onClick = { viewModel.limpiarMensaje() }) { Text("×") }
                    }
                }
            }
            contenido()
        }
    }
}

@Composable
fun Accion(texto: String, onClick: () -> Unit, habilitado: Boolean = true) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp), enabled = habilitado) {
        Text(texto, fontSize = 16.sp)
    }
}

@Composable
fun Tarjeta(titulo: String, detalle: String, onClick: (() -> Unit)? = null) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick ?: {}, enabled = onClick != null) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(detalle, fontSize = 13.sp)
        }
    }
}

@Composable
fun Seccion(texto: String) { Text(texto, fontSize = 19.sp, fontWeight = FontWeight.SemiBold) }
