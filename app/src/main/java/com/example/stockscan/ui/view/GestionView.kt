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
fun GestionView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    Pantalla(navController, viewModel, "Administrar tiendas") {
        if(e.perfil != Perfil.GERENCIAL) { Text("Acceso exclusivo de gerencia"); return@Pantalla }
        Seccion("${e.tiendas.size} tiendas de prueba")
        e.tiendas.forEach { t ->
            Tarjeta(t.nombre, "${t.id} · ${t.bodega} · ${if(t.habilitada) "Habilitada" else "Deshabilitada"}")
            Accion(if (t.habilitada) "Deshabilitar ${t.id}" else "Habilitar ${t.id}", { viewModel.cambiarEstadoTienda(t.id) })
        }
        Text("Deshabilitar bloquea nuevos movimientos y conteos. El historial permanece disponible.")
        Seccion("Administrar productos")
        e.productos.forEach { p ->
            Tarjeta(p.nombre, "${p.codigoInterno} · ${p.codigoBarras} · mín. ${p.stockMinimo}") {
                viewModel.seleccionarProducto(p.codigoInterno)
                navController.irA(Destinos.GestionProducto.ruta)
            }
        }
        Accion("Crear producto", {
            viewModel.seleccionarProducto("")
            navController.irA(Destinos.GestionProducto.ruta)
        })
    }
}
