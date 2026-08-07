package com.sxisco.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sxisco.app.ui.theme.SxiscoTextSecondary

/**
 * Ainda nao ligado a um gerenciador de pacotes de verdade (isso e um
 * projeto a parte: precisa de um indice de libs + integracao com
 * dpkg/apt dentro de um userland tipo Termux). Por enquanto so mostra
 * a intencao da tela.
 */
@Composable
fun PackagesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Packages", style = MaterialTheme.typography.titleLarge)
        Text(
            "Ainda nao conectado a um repositorio de pacotes de verdade. " +
                "Isso entra numa proxima etapa.",
            style = MaterialTheme.typography.bodyMedium,
            color = SxiscoTextSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
