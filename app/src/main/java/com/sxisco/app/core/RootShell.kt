package com.sxisco.app.core

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

/**
 * Sessao persistente de root: abre UM processo "su" e mantem stdin/stdout
 * abertos, entao cada exec() roda no MESMO shell (cd, variaveis etc
 * continuam validos entre chamadas, igual um terminal raiz de verdade).
 *
 * Exige o aparelho ter root (Magisk/KernelSU/etc). Na primeira chamada
 * de start(), o gerenciador de superusuario mostra o popup de permissao
 * pro usuario aceitar.
 */
class RootShell {

    private var process: Process? = null
    private var stdin: DataOutputStream? = null
    private var stdout: BufferedReader? = null

    val isActive: Boolean
        get() = process != null

    /** Abre a sessao su. Retorna false se root foi negado ou nao existe su. */
    fun start(): Boolean {
        return try {
            val proc = Runtime.getRuntime().exec("su")
            stdin = DataOutputStream(proc.outputStream)
            stdout = BufferedReader(InputStreamReader(proc.inputStream))
            process = proc

            // se o su foi negado, o processo normalmente morre aqui;
            // esse echo confirma que a sessao esta viva e respondendo
            val check = exec("echo sxisco_root_ok")
            if (!check.contains("sxisco_root_ok")) {
                close()
                return false
            }
            true
        } catch (e: Exception) {
            close()
            false
        }
    }

    /**
     * Roda um comando na sessao root ativa e devolve tudo que ele
     * imprimiu. Bloqueante — chame sempre fora da thread principal.
     */
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
