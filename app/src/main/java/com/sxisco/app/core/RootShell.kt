package com.sxisco.app.core

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

class RootShell {

    private var process: Process? = null
    private var stdin: DataOutputStream? = null
    private var stdout: BufferedReader? = null

    val isActive: Boolean
        get() = process != null

    fun start(): Boolean {
        return try {
            val proc = Runtime.getRuntime().exec("su")
            stdin = DataOutputStream(proc.outputStream)
            stdout = BufferedReader(InputStreamReader(proc.inputStream))
            process = proc

            val check = exec("echo sxisco_root_ok")
            if (!check.contains("sxisco_root_ok")) {
                close()
                return false
            }

            // procura um busybox de verdade (Magisk, KernelSU, instalacao
            // manual) e coloca ele na frente do PATH - sem isso o shell
            // padrao do Android (toybox) so tem uns 20-30 comandos
            exec(
                """
                for bb in /data/adb/magisk/busybox /data/adb/ksu/bin/busybox /data/adb/ap/bin/busybox /system/xbin/busybox /system/bin/busybox; do
                    if [ -x "${'$'}bb" ]; then
                        export PATH="${'$'}(dirname ${'$'}bb)":${'$'}PATH
                        alias ls='busybox ls' 2>/dev/null
                        break
                    fi
                done
                """.trimIndent()
            )

            true
        } catch (e: Exception) {
            close()
            false
        }
    }

    fun exec(command: String): String {
        val input = stdin ?: return "erro: sessao root nao iniciada"
        val output = stdout ?: return "erro: sessao root nao iniciada"
        val marker = "__SXISCO_END_${System.nanoTime()}__"

        return try {
            input.writeBytes("$command\n")
            input.writeBytes("echo $marker\n")
            input.flush()

            val builder = StringBuilder()
            var line: String?
            while (true) {
                line = output.readLine() ?: break
                if (line == marker) break
                builder.appendLine(line)
            }
            builder.toString().trimEnd()
        } catch (e: Exception) {
            "erro ao executar comando: ${e.message}"
        }
    }

    fun close() {
        try {
            stdin?.writeBytes("exit\n")
            stdin?.flush()
        } catch (_: Exception) {
        }
        try {
            process?.destroy()
        } catch (_: Exception) {
        }
        process = null
        stdin = null
        stdout = null
    }
}
