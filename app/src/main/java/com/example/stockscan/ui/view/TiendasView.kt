package com.example.stockscan.ui.view

    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Button
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.compose.collectAsStateWithLifecycle
    import androidx.navigation.NavController
    import com.example.stockscan.modelo.Destinos
    import com.example.stockscan.viewmodel.StockScanViewModel

    @Composable
    fun TiendasView(navController: NavController, viewModel: StockScanViewModel) {
        val estado by viewModel.uiState.collectAsStateWithLifecycle()
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
            Text("Elige tu tienda", fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text("${estado.perfil.descripcion} · ${estado.usuario}")
            Spacer(modifier = Modifier.height(18.dp))
            Text("¿Dónde vas a trabajar hoy?", fontSize = 19.sp)
            Text("La tienda elegida define tus productos y conteos.")
            Spacer(modifier = Modifier.height(18.dp))
            estado.tiendas.forEach { tienda ->
                Button(
                    onClick = {
                        if (viewModel.seleccionarTienda(tienda.id)) {
                            navController.navigate(Destinos.Inicio.ruta)
                        }
                    },
                    enabled = tienda.habilitada,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).height(65.dp)
                ) {
                    val estadoTienda = if (tienda.habilitada) "Habilitada" else "Deshabilitada"
                    Text("${tienda.nombre} · ${tienda.id} · ${tienda.bodega}\n$estadoTienda")
                }
            }
            Text("Las tiendas deshabilitadas no permiten movimientos ni conteos.",
                fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
            if (estado.mensaje != null) {
                Text(estado.mensaje!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
