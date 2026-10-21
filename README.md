# StockScan · Grupo 6

**Proyecto Android Studio (Kotlin / Jetpack Compose), basado en los tres ejemplos de clases del profesor y en la documentación del caso DSY1105.**

> **ESTADO: PROTOTIPO DEMO LOCAL. No es un MVP conectado a la API.**
> El backend Spring Boot, el bloqueo real por ausencia de red y la persistencia remota **siguen pendientes**. No confundir los datos de memoria con inventario sincronizado.

## Abrir y ejecutar

1. Descomprime el ZIP y selecciona la carpeta **`StockScan`** en Android Studio > **Open**.
2. Deja que Gradle sincronice (este proyecto usa **compileSdk 37**, igual que los ZIP de clase). La máquina debe tener instalado el SDK 37 y acceso para resolver las dependencias de CameraX y ML Kit.
3. Ejecuta el módulo **app** en un teléfono Android con cámara o en un emulador; la cámara requiere un dispositivo con entrada disponible.
4. Perfiles de prueba: `OP-007` (operador), `EB-001` (bodega), `GE-001` (gerencial); contraseña **`Demo-2026`** para los tres.
5. Elige una tienda (T-01 Norte, T-02 Centro o T-03 Sur). T-04 Reserva empieza deshabilitada.

**Limitación de verificación:** se compilaron por separado los modelos y reglas de negocio con el compilador Kotlin y se ejecutaron 12 comprobaciones de modelos y 25 comprobaciones adicionales de flujos ViewModel, con un stub de AndroidX para pruebas JVM. En este entorno no hay Android SDK ni Gradle instalado para compilar y ejecutar toda la app; debes verificar la compilación y permisos de cámara en Android Studio antes de entrega.

## Flujos de prueba recomendados

**Operador:**
- Accede con `OP-007` / `Demo-2026`, elige T-01 y consulta `2000000000015`.
- Stock inicial P-001 en T-01: 8 u. Ejecuta **Entrada 12**; el resultado local es 20 u. y genera un movimiento.
- Inicia un conteo. Fija P-001=18, P-002=24, P-003=42. Revisa las dos diferencias (−2 y +2), confirma y observa historial/alertas. El total inicial T-01 es 72 u.; luego de la entrada y conteo es 84 u.
- El botón **Leer con la cámara** usa CameraX y ML Kit. Alternativa: introducir uno de los tres códigos de barras de prueba.

**Encargado de bodega:**
- Accede con `EB-001`; consulta stock y alertas de la tienda, declara reposición con motivo; esa acción no suma existencias. Puede resolver alertas manuales.

**Gerencial:**
- Accede con `GE-001`; revisa resumen por tiendas habilitadas, administra estado de tienda, crea/edita catálogo, consulta historial y exporta CSV a una carpeta que elige el usuario.
- El botón `.xlsx` solo muestra la **simulación** del mockup: **no genera un Excel real**.

## Organización basada en las clases del profesor

- `MainActivity.kt`: `NavHost`, `composable` y `rememberNavController`, como `MyApplication002_FIN`.
- `modelo/Destinos.kt`: `sealed class Destinos` y rutas, como `MyApplication002_FIN`.
- `model/StockScanModel.kt`: `data class` y estado de pantalla, como `Ejemplo22_Fin`.
- `viewmodel/StockScanViewModel.kt`: `ViewModel`, `MutableStateFlow`, `StateFlow`, `asStateFlow()` y `_uiState.update { it.copy(...) }`, como `Ejemplo22_Fin`.
- `ui/view/*.kt`: `@Composable`, formularios, botones, `collectAsStateWithLifecycle`, como ambos ejemplos Android.
- `model/InventarioReglas.kt`: validaciones simples y separadas para poder probarlas sin Android.
- `ui/view/CamaraCodigoBarras.kt`: excepción justificada por **RF03 y la pauta**, que exigen cámara real. No está en los ejemplos del profesor, por eso está aislada del resto.
- `EcoDrive_FIN`: referencia adicional de clases, datos y métodos simples; el proyecto de Android mantiene la estructura del ejemplo más pertinente (`Ejemplo22_FIN`).

## Requisitos y evaluación

Consulta **`CUMPLIMIENTO_PAUTA.md`**: matriz de RF01–RF10, RNF01–RNF04 y restricciones del caso, indicando **implementado en demo**, **parcial** o **pendiente**. No se asignan puntajes ni criterios inventados.

## Datos de demostración

- T-01: 72 u. inicialmente, T-02: 126 u., T-03: 90 u. ⇒ 288 u. en total. El mockup exhibe 300 u. **después** de una entrada de +12 en T-01.
- T-04 no tiene stock y comienza deshabilitada.
- P-001 2000000000015 · P-002 2000000000022 · P-003 2000000000039.
- Registros y alertas se conservan solo mientras la aplicación permanezca abierta. No hay sincronización real; esto es explícitamente **una fase previa** al backend.

## Inconsistencia que aparece en los documentos

La propuesta del Grupo 6 identifica a bodega como `EB-001`; el mockup de una página lo escribe `BG-003`. Esta versión usa **`EB-001` según la propuesta ERS**. El profesor debe confirmar el identificador definitivo.
