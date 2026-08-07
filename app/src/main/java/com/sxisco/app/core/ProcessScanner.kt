package com.sxisco.app.core

import android.content.Context
import com.sxisco.app.data.RunningProcess

/**
 * Le a lista de processos em execucao via /proc (precisa de root pra ver
 * processos de outros apps - a partir do Android 11 o kernel esconde
 * /proc/<pid> de UID diferente do seu, hidepid=2 na maioria dos aparelhos).
 *
 * Cada linha devolvida pelo shell vem no formato "pid|uid|cmdline",
 * depois resolvemos pacote/nome via PackageManager.getPackagesForUid.
 */
class ProcessScanner(
    private val context: Context,
    private val shell: RootShell
) {

    fun listRunningApps(): List<RunningProcess> {
        val pm = context.packageManager

        val script = """
            for p in /proc/[0-9]*; do
                pid=${'$'}{p#/proc/}
                uid=${'$'}(stat -c %u "${'$'}p" 2>/dev/null)
                cmd=${'$'}(tr '\0' ' ' < "${'$'}p/cmdline" 2>/dev/null)
                if [ -n "${'$'}uid" ] && [ -n "${'$'}cmd" ]; then
                    echo "${'$'}pid|${'$'}uid|${'$'}cmd"
                fi
            done
        """.trimIndent()

        val raw = shell.exec(script)
        val result = mutableListOf<RunningProcess>()

        raw.lineSequence().forEach { line ->
            val parts = line.split("|", limit = 3)
            if (parts.size < 3) return@forEach

            val pid = parts[0].trim().toIntOrNull() ?: return@forEach
            val uid = parts[1].trim().toIntOrNull() ?: return@forEach
            val cmd = parts[2].trim()

            // so nos interessam processos de apps de verdade (nome de pacote
            // tem ponto, ex: com.discord); kernel threads e daemons ficam fora
            if (cmd.isBlank() || !cmd.contains('.')) return@forEach

            val packages = try {
                pm.getPackagesForUid(uid)
            } catch (e: SecurityException) {
                null
            } ?: return@forEach

            val packageName = packages.firstOrNull { cmd.startsWith(it) } ?: packages.first()

            val label = try {
                val appInfo = pm.getApplicationInfo(packageName, 0)
                pm.getApplicationLabel(appInfo).toString()
            } catch (e: Exception) {
                packageName
            }

            result += RunningProcess(pid = pid, uid = uid, packageName = packageName, label = label)
        }

        return result.distinctBy { it.pid }.sortedBy { it.label.lowercase() }
    }
}
