package com.sxisco.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sxisco.app.core.Key
import com.sxisco.app.core.Lang
import com.sxisco.app.core.t
import com.sxisco.app.ui.theme.SxiscoTextSecondary

@Composable
fun PackagesScreen(lang: Lang) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(t(lang, Key.PACKAGES_TITLE), style = MaterialTheme.typography.titleLarge)
        Text(
            t(lang, Key.PACKAGES_MSG),
            style = MaterialTheme.typography.bodyMedium,
            color = SxiscoTextSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
