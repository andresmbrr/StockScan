package com.example.stockscan.ui.view

import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

/** CameraX + ML Kit: solo lectura de códigos, sin persistir imágenes. */
@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun CamaraCodigoBarras(onCodigo: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val capturado = remember { AtomicBoolean(false) }
    val scanner = remember {
        BarcodeScanning.getClient(BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_EAN_13, Barcode.FORMAT_CODE_128, Barcode.FORMAT_QR_CODE)
            .build())
    }
    val vista = remember { PreviewView(context) }
    DisposableEffect(lifecycleOwner) {
        val futuro = ProcessCameraProvider.getInstance(context)
        var proveedor: ProcessCameraProvider? = null
        futuro.addListener({
            try {
                proveedor = futuro.get()
                val preview = Preview.Builder().build().apply { surfaceProvider = vista.surfaceProvider }
                val analisis = ImageAnalysis.Builder().setTargetResolution(Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
                analisis.setAnalyzer(ContextCompat.getMainExecutor(context)) { frame ->
                    val imagen = frame.image
                    if (imagen == null || capturado.get()) { frame.close() }
                    else {
                        scanner.process(InputImage.fromMediaImage(imagen, frame.imageInfo.rotationDegrees))
                            .addOnSuccessListener { codigos ->
                                val codigo = codigos.firstOrNull()?.rawValue
                                if (!codigo.isNullOrBlank() && capturado.compareAndSet(false, true)) onCodigo(codigo)
                            }
                            .addOnCompleteListener { frame.close() }
                    }
                }
                proveedor?.unbindAll()
                proveedor?.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analisis)
            } catch (_: Exception) { /* Permiso, equipo sin cámara o cámara ocupada: queda disponible el ingreso manual. */ }
        }, ContextCompat.getMainExecutor(context))
        onDispose { proveedor?.unbindAll(); scanner.close() }
    }
    AndroidView(factory = { vista }, modifier = Modifier.fillMaxWidth().height(250.dp))
}
