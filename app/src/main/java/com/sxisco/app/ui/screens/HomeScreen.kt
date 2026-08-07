package com.sxisco.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sxisco.app.core.Key
import com.sxisco.app.core.Lang
import com.sxisco.app.core.t
import com.sxisco.app.data.RunningProcess
import com.sxisco.app.ui.theme.SxiscoBorder
import com.sxisco.app.ui.theme.SxiscoTextSecondary

@Composable
fun HomeScreen(
    lang: Lang,
    rootGranted: Boolean,
    loading: Boolean,
    processes: List<RunningProcess>,
    onRefresh: () -> Unit,
    onOpenProcess: (RunningProcess) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(t(lang, Key.HOME_TITLE), style = MaterialTheme.typography.titleLarge)
                Text(
                    text = when {
                        !rootGranted -> t(lang, Key.HOME_ROOT_NOT_GRANTED)
                        loading -> t(lang, Key.HOME_SCANNING)
                        else -> "${processes.size} ${t(lang, Key.HOME_APPS_RUNNING)}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = SxiscoTextSecondary
                )
            }
            IconButton(onClick = onRefresh, enabled = rootGranted && !loading) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
            }
        }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            when {
                !rootGranted -> {
                    Text(
                        t(lang, Key.HOME_ROOT_MSG),
                        color = SxiscoTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(24.dp))
                }
                processes.isEmpty() -> {
                    Text(t(lang, Key.HOME_NO_PROCESSES), color = SxiscoTextSecondary)
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, SxiscoBorder, RoundedCornerShape(16.dp))
                    ) {
                        processes.forEachIndexed { index, proc ->
                            ProcessRow(proc = proc, onClick = { onOpenProcess(proc) })
                            if (index != processes.lastIndex) {
                                Row(Modifier.fillMaxWidth().background(SxiscoBorder).height(1.dp)) {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProcessRow(proc: RunningProcess, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = proc.label.take(1).uppercase(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Text(proc.label, style = MaterialTheme.typography.titleMedium)
            Text(
                "${proc.packageName} - pid ${proc.pid}",
                style = MaterialTheme.typography.bodySmall,
                color = SxiscoTextSecondary
            )
        }
    }
}
