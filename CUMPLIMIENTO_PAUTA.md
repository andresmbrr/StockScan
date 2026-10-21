# Matriz de cumplimiento — StockScan Grupo 6

Fuentes revisadas: **002_Formato_Caso_MultiTienda (3 págs.)**, **Propuesta Solucion StockScan Grupo 6 (3 págs., apartado 5)** y **mockup StockScan (1 pág.)**. No se recibió una rúbrica de puntajes separada. Los siguientes son los requisitos que esos archivos sí documentan.

| Requisito del ERS | Implementación en esta versión | Evaluación verificable |
|---|---|---|
| RF01 — Acceso por roles de prueba | `LoginView`, `StockScanViewModel`, datos ficticios | Demo implementada |
| RF02 — Selección y cambio de tienda | `TiendasView`, bloqueo T-04 por defecto | Demo implementada |
| RF03 — Cámara + código manual | `EscanearView`, `CamaraCodigoBarras` con CameraX/ML Kit y alternativa manual | Código incorporado; falta prueba física de cámara |
| RF04 — Detalle producto y stock por tienda | `ProductosView`, `DetalleView` | Demo implementada |
| RF05 — Entradas, salidas y ajustes, confirmación y validaciones | `MovimientoView`, `InventarioReglas`; impide negativos; ajuste requiere motivo | Demo implementada; sin API |
| RF06 — Conteo con reloj, repetición y comparador | `ConteoView`, tiempo transcurrido, entrada por código y acumulador | Demo implementada; sin API |
| RF07 — Confirmar diferencias fija nuevo stock | `ResumenConteoView`, `confirmarConteo` crea movimientos solo para diferencias | Demo implementada; sin API |
| RF08 — Alertas de mínimo, descalce y reposición | `AlertasView`, reglas y resolución manual | Demo implementada; sin API |
| RF09 — Gerencial: tiendas, productos e historial inmutable | `GestionView`, `GestionProductoView`, `HistorialView` | Crear/editar productos, habilitar/deshabilitar tiendas; historial solo en memoria |
| RF10 — Exportación Excel/CSV local | `ReportesView`, `ExportarCsv` guarda CSV mediante selector del sistema | CSV implementado; `.xlsx` **simulado sin crear archivo**, según mockup |
| RNF01 — Kotlin / Compose / MVVM y backend REST Spring Boot | Android estructurado en ViewModel/Model/View | Android implementado; **Spring Boot/Retrofit y persistencia REST pendientes** |
| RNF02 — Sin conexión bloqueada, sin modo offline | La demo funciona sin servidor como prototipo anterior a integración | **Pendiente**: la versión demo no cumple conexión obligatoria |
| RNF03 — Legibilidad, botones altos y navegación según rol | Tema contrastado, botones primarios ≥56 dp, barra inferior por rol | Implementación inicial; requiere prueba de accesibilidad |
| RNF04 — Privacidad y datos sintéticos | Códigos anónimos y productos ficticios; no fotos guardadas ni usuarios reales | Incorporado como criterio de desarrollo; revisión legal formal fuera de alcance |

## Restricciones adicionales del caso

- **Android Studio, Kotlin, MVVM, Jetpack Compose y GitHub:** proyecto organizado para Android Studio; GitHub aún no actualizado desde esta sesión por error 403 de integración.
- **Backend Spring Boot como fuente de verdad:** aún NO implementado. Todas las operaciones actuales solo cambian memoria del dispositivo.
- **Internet permanente / sincronización inmediata:** NO implementado. Debe bloquearse inventario y movimientos si falla la API.
- **Reporte XLSX académico:** mockup de simulación; se agregó guardado real **CSV** como mejora local simple y explícita.
- **Sin credenciales de producción ni ERPs:** cumple en esta versión (exclusivamente ficticio).
- **Historial UTC:** se registra con cadena UTC; sin persistencia, se pierde al reiniciar.

## Qué falta antes de considerar cumplido el caso académico completo

1. Crear backend Spring Boot REST para tiendas, productos, stock, movimientos, conteos y alertas con persistencia real de los datos de prueba.
2. Conectar Android a ese backend vía Retrofit2; mostrar y bloquear al perder conexión, rechazar duplicados desde servidor, operar únicamente tras respuesta exitosa.
3. Probar cámara y permisos en Android real; probar sincronización y manejo de respuestas API.
4. Confirmar si se exige **generar `.xlsx` real** o basta la simulación definida en mockup.
5. Ejecutar Gradle/build y pruebas Android instrumentadas en Android Studio; revisar la pauta oficial si el docente proporciona otra rúbrica.
