package com.example.stockscan.ui.view

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.stockscan.model.Perfil
import com.example.stockscan.modelo.Destinos
import com.example.stockscan.viewmodel.StockScanViewModel

@Composable
fun LoginView(navController: NavController, viewModel: StockScanViewModel) {
    val formulario by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("StockScan", fontSize = 32.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Text("Multi-Tienda", fontSize = 17.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Tu inventario, siempre al día", fontSize = 24.sp,
            fontWeight = FontWeight.Bold)
        Text("Consulta el stock de cada tienda con datos de prueba.")
        Spacer(modifier = Modifier.height(24.dp))
        Text("Acceso de prueba", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Perfil.entries.forEach { perfil ->
            Button(
                onClick = { viewModel.seleccionarPerfil(perfil) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                val marca = if (formulario.perfil == perfil) "✓ " else ""
                Text("$marca${perfil.descripcion} · ${perfil.codigo}")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = formulario.usuario,
            onValueChange = { viewModel.onUsuarioChange(it) },
            label = { Text("Usuario ficticio") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = formulario.clave,
            onValueChange = { viewModel.onClaveChange(it) },
            label = { Text("Clave ficticia") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Clave de demostración: Demo-2026", fontSize = 13.sp)
        if (formulario.mensaje != null) {
            Text(formulario.mensaje!!, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            onClick = {
                if (viewModel.ingresar()) {
                    navController.navigate(Destinos.Tiendas.ruta)
                }
            }
        ) { Text("Acceder como ${formulario.perfil.codigo}") }
        Spacer(modifier = Modifier.height(12.dp))
        Text("DSY1105 · MVP académico · Solo datos ficticios", fontSize = 12.sp)
    }
}
