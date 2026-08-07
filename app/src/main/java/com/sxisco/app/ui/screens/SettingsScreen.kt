package com.sxisco.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sxisco.app.core.Key
import com.sxisco.app.core.Lang
import com.sxisco.app.core.t
import com.sxisco.app.ui.theme.SxiscoTextSecondary

@Composable
fun SettingsScreen(lang: Lang, rootGranted: Boolean, onLanguageChange: (Lang) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(t(lang, Key.SETTINGS_TITLE), style = MaterialTheme.typography.titleLarge)
        Text(
            t(lang, Key.SETTINGS_VERSION),
            style = MaterialTheme.typography.bodyMedium,
            color = SxiscoTextSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            if (rootGranted) t(lang, Key.SETTINGS_ROOT_YES) else t(lang, Key.SETTINGS_ROOT_NO),
            style = MaterialTheme.typography.bodyMedium,
            color = SxiscoTextSecondary
        )

        Text(
            t(lang, Key.SETTINGS_LANGUAGE),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
        )
        Row {
            OutlinedButton(onClick = { onLanguageChange(Lang.EN) }, modifier = Modifier.padding(end = 8.dp)) {
                Text("English")
            }
            OutlinedButton(onClick = { onLanguageChange(Lang.ES) }, modifier = Modifier.padding(end = 8.dp)) {
                Text("Español")
            }
            OutlinedButton(onClick = { onLanguageChange(Lang.PT) }) {
                Text("Português")
            }
        }
    }
}
