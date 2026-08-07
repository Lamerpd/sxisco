package com.sxisco.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.sxisco.app.core.RootShell
import com.sxisco.app.data.RunningProcess
import com.sxisco.app.ui.theme.SxiscoTextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Terminal real: cada comando digitado roda de fato na sessao root
 * (RootShell), com o pid/pacote do app selecionado mostrados no topo
 * como contexto. Nao e um namespace isolado de verdade (Android nao
 * expoe isso facilmente sem ferramentas tipo nsenter compiladas a
 * parte) - e um shell root com foco visual/de contexto naquele app,
 * de onde da pra inspecionar /proc/<pid>, /data/data/<pkg> etc.
 */
@Composable
fun TerminalScreen(
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
            shell.exec("echo pid ${process.pid} -- cwd: \$(readlink /proc/${process.pid}/cwd 2>/dev/null)")
        }.let { out ->
            if (out.isNotBlank()) lines.add(out)
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
                Text("< voltar")
            }
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(process.label, style = MaterialTheme.typography.titleMedium)
                Text(
                    "pid ${process.pid} - escopado",
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
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("comando root...") },
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
                Text("Enviar")
            }
        }
    }
}
