package com.sxisco.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sxisco.app.core.Key
import com.sxisco.app.core.Lang
import com.sxisco.app.core.RootShell
import com.sxisco.app.core.t
import com.sxisco.app.data.RunningProcess
import com.sxisco.app.ui.theme.SxiscoTextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TerminalScreen(
    lang: Lang,
    process: RunningProcess,
    shell: RootShell,
    onBack: () -> Unit
) {
    val lines = remember { mutableStateListOf<String>() }
    var input by remember { mutableStateOf("") }
    var running by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(process.pid) {
        lines.clear()
        lines.add("sessao escopada: ${process.label} (${process.packageName})")
        withContext(Dispatchers.IO) {
            val script = """
                cd "${'$'}(readlink -f /proc/${process.pid}/cwd 2>/dev/null)" 2>/dev/null && pwd || (cd /data/data/${process.packageName} 2>/dev/null && pwd) || echo 'nao foi possivel entrar no diretorio do processo'
            """.trimIndent()
            shell.exec(script)
        }.let { out ->
            if (out.isNotBlank()) lines.add(out)
        }
    }

    LaunchedEffect(lines.size) {
        if (lines.isNotEmpty()) {
            listState.animateScrollToItem(lines.size - 1)
        }
    }

    fun runCommand(cmd: String) {
        if (cmd.isBlank() || running) return
        lines.add("$ $cmd")
        running = true
        scope.launch {
            val output = withContext(Dispatchers.IO) { shell.exec(cmd) }
            if (output.isNotBlank()) lines.add(output)
            running = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text(t(lang, Key.TERMINAL_BACK))
            }
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(process.label, style = MaterialTheme.typography.titleMedium)
                Text(
                    "pid ${process.pid} - ${t(lang, Key.TERMINAL_SCOPED)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SxiscoTextSecondary
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            items(lines) { line ->
                Text(
                    text = line,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(t(lang, Key.TERMINAL_PLACEHOLDER)) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    runCommand(input)
                    input = ""
                })
            )
            TextButton(onClick = {
                runCommand(input)
                input = ""
            }) {
                Text(t(lang, Key.TERMINAL_SEND))
            }
        }
    }
}
