package com.example.stockscan.model

/** Exportación CSV real local de datos ficticios; el mockup XLSX es únicamente simulado. */
object ExportarCsv {
    private fun texto(s: String): String = "\"${s.replace("\"", "\"\"").replace("\n", " ")}\""
    fun stock(e: StockScanModel): String = buildString {
        appendLine("tienda,codigo,nombre,categoria,stock,minimo")
        e.tiendas.filter { it.habilitada }.forEach { t ->
            e.productos.forEach { p ->
                appendLine(listOf(t.id, p.codigoInterno, texto(p.nombre), texto(p.categoria),
                    e.stockDe(p, t.id).toString(), p.stockMinimo.toString()).joinToString(","))
            }
        }
    }
    fun historial(e: StockScanModel): String = buildString {
        appendLine("id,tienda,producto,tipo,diferencia,anterior,nuevo,usuario,fecha_utc,motivo")
        e.movimientos.forEach { m ->
            appendLine(listOf(m.id, m.tiendaId, m.productoId, m.tipo.name, m.cantidad,
                m.anterior, m.nuevo, m.usuario, m.fechaUtc, texto(m.motivo)).joinToString(","))
        }
    }
}
