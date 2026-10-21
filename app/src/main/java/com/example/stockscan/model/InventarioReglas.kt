package com.example.stockscan.model

/** Reglas simples y comprobables, sin dependencias de Android. */
object InventarioReglas {
    fun calcularNuevoStock(actual: Int, tipo: TipoMovimiento, cantidad: Int): Int? {
        if (actual < 0) return null
        val nuevo = when (tipo) {
            TipoMovimiento.ENTRADA -> if (cantidad > 0) actual + cantidad else return null
            TipoMovimiento.SALIDA -> if (cantidad > 0) actual - cantidad else return null
            TipoMovimiento.AJUSTE -> if (cantidad != 0) actual + cantidad else return null
            TipoMovimiento.CONTEO -> if (cantidad >= 0) cantidad else return null
        }
        return nuevo.takeIf { it >= 0 }
    }
    fun validarMovimiento(actual: Int, tipo: TipoMovimiento, cantidad: Int?, motivo: String): String? {
        if (cantidad == null) return "Ingresa una cantidad entera"
        if (tipo == TipoMovimiento.AJUSTE && motivo.isBlank()) return "El ajuste necesita un motivo"
        if (calcularNuevoStock(actual, tipo, cantidad) == null) {
            return if (tipo == TipoMovimiento.SALIDA && cantidad > actual) "Stock insuficiente"
            else "Cantidad inválida para ${tipo.titulo.lowercase()}"
        }
        return null
    }
}
