package com.example.stockscan.modelo

// Siguiendo el archivo Destinos.kt entregado por el profesor.
sealed class Destinos(val ruta: String) {
    object Login: Destinos("login")
    object Tiendas: Destinos("tiendas")
    object Inicio: Destinos("inicio")
    object Escanear: Destinos("escanear")
    object Productos: Destinos("productos")
    object Detalle: Destinos("detalle")
    object Movimiento: Destinos("movimiento")
    object Conteo: Destinos("conteo")
    object ResumenConteo: Destinos("resumen_conteo")
    object Alertas: Destinos("alertas")
    object Historial: Destinos("historial")
    object Resumen: Destinos("resumen")
    object Gestion: Destinos("gestion")
    object GestionProducto: Destinos("gestion_producto")
    object Reportes: Destinos("reportes")
}
