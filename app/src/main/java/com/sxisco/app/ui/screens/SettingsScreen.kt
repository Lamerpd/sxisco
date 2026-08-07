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

@Composable
fun SettingsScreen(rootGranted: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Ajustes", style = MaterialTheme.typography.titleLarge)
        Text(
            "sxisco - versao 0.1",
            style = MaterialTheme.typography.bodyMedium,
            color = SxiscoTextSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            if (rootGranted) "Root: concedido" else "Root: nao concedido",
            style = MaterialTheme.typography.bodyMedium,
            color = SxiscoTextSecondary
        )
    }
}
