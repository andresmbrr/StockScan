package com.example.stockscan.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.Perfil
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun InicioView(navController: NavController, viewModel: StockScanViewModel) {
    val e by viewModel.uiState.collectAsStateWithLifecycle()
    Pantalla(navController, viewModel, if (e.perfil == Perfil.GERENCIAL) "StockScan · Resumen" else "StockScan") {
        Seccion(e.tiendaActual()?.nombre ?: "Selecciona una tienda")
        Text("${e.tiendaActual()?.bodega ?: "-"} · Tu inventario")
        Tarjeta("${e.productos.size} productos únicos", "${e.totalUnidades()} unidades registradas en la demo")
        Tarjeta("${e.alertasActivas().count { it.tiendaId == e.tiendaId }} alertas", "Avisos de stock mínimo, diferencias y reposición")
        if (e.perfil == Perfil.OPERADOR) {
            Accion("Escanear producto", { navController.irA(Destinos.Escanear.ruta) })
            Accion("Registrar movimiento", { navController.irA(Destinos.Productos.ruta) })
            Accion("Conteo físico", { if (viewModel.iniciarConteo()) navController.irA(Destinos.Conteo.ruta) })
            Accion("Historial de tienda", { navController.irA(Destinos.Historial.ruta) })
        } else if (e.perfil == Perfil.BODEGA) {
            Accion("Consultar productos", { navController.irA(Destinos.Productos.ruta) })
            Accion("Alertas y reposiciones", { navController.irA(Destinos.Alertas.ruta) })
        } else {
            Accion("Ver resumen global", { navController.irA(Destinos.Resumen.ruta) })
            Accion("Administrar tiendas y productos", { navController.irA(Destinos.Gestion.ruta) })
            Accion("Reportes", { navController.irA(Destinos.Reportes.ruta) })
        }
        Accion("Cambiar tienda", { navController.irA(Destinos.Tiendas.ruta) })
        Accion("Cerrar sesión", {
            viewModel.cerrarSesion()
            navController.navigate(Destinos.Login.ruta) { popUpTo(0) }
        })
    }
}
