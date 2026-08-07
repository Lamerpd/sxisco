package com.sxisco.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sxisco.app.core.BackgroundMusicPlayer
import com.sxisco.app.core.ProcessScanner
import com.sxisco.app.core.RootShell
import com.sxisco.app.data.RunningProcess
import com.sxisco.app.ui.screens.HomeScreen
import com.sxisco.app.ui.screens.PackagesScreen
import com.sxisco.app.ui.screens.SettingsScreen
import com.sxisco.app.ui.screens.TerminalScreen
import com.sxisco.app.ui.theme.SxiscoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val rootShell = RootShell()
    private lateinit var musicPlayer: BackgroundMusicPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        musicPlayer = BackgroundMusicPlayer(this, R.raw.background_music)
        musicPlayer.start()
        setContent {
            SxiscoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SxiscoApp(shell = rootShell, musicPlayer = musicPlayer)
                }
            }
        }
    }

    override fun onDestroy() {
        rootShell.close()
        musicPlayer.release()
        super.onDestroy()
    }
}

private enum class Tab { HOME, PACKAGES, SETTINGS }

@Composable
fun SxiscoApp(shell: RootShell, musicPlayer: BackgroundMusicPlayer) {
    var rootGranted by remember { mutableStateOf(false) }
    var rootChecked by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var processes by remember { mutableStateOf<List<RunningProcess>>(emptyList()) }
    var selectedProcess by remember { mutableStateOf<RunningProcess?>(null) }
    var tab by remember { mutableStateOf(Tab.HOME) }
    var muted by remember { mutableStateOf(musicPlayer.isMuted) }

    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    val scanner = remember { ProcessScanner(context, shell) }

    fun refresh() {
        if (!rootGranted) return
        loading = true
        scope.launch {
            val result = withContext(Dispatchers.IO) { scanner.listRunningApps() }
            processes = result
            loading = false
        }
    }

    LaunchedEffect(Unit) {
        val granted = withContext(Dispatchers.IO) { shell.start() }
        rootGranted = granted
        rootChecked = true
        if (granted) {
            loading = true
            val result = withContext(Dispatchers.IO) { scanner.listRunningApps() }
            processes = result
            loading = false
        }
    }

    val current = selectedProcess
    if (current != null) {
        TerminalScreen(
            process = current,
            shell = shell,
            onBack = { selectedProcess = null }
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            when (tab) {
                Tab.HOME -> HomeScreen(
                    rootGranted = rootGranted,
                    loading = loading || !rootChecked,
                    processes = processes,
                    onRefresh = { refresh() },
                    onOpenProcess = { selectedProcess = it }
                )
                Tab.PACKAGES -> PackagesScreen()
                Tab.SETTINGS -> SettingsScreen(rootGranted = rootGranted)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
                .padding(8.dp)
        ) {
            TextButton(onClick = { tab = Tab.HOME }, modifier = Modifier.weight(1f)) {
                Text("Processos")
            }
            TextButton(onClick = { tab = Tab.PACKAGES }, modifier = Modifier.weight(1f)) {
                Text("Packages")
            }
            TextButton(onClick = { tab = Tab.SETTINGS }, modifier = Modifier.weight(1f)) {
                Text("Ajustes")
            }
            TextButton(onClick = {
                musicPlayer.toggleMute()
                muted = musicPlayer.isMuted
            }) {
                Text(if (muted) "Som: off" else "Som: on")
            }
        }
    }
}
