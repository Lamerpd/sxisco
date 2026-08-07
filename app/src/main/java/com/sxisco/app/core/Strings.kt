package com.sxisco.app.core

enum class Lang { EN, ES, PT }

enum class Key {
    TAB_PROCESSES, TAB_PACKAGES, TAB_SETTINGS, SOUND_ON, SOUND_OFF,
    HOME_TITLE, HOME_ROOT_NOT_GRANTED, HOME_SCANNING, HOME_APPS_RUNNING,
    HOME_ROOT_MSG, HOME_NO_PROCESSES,
    SETTINGS_TITLE, SETTINGS_VERSION, SETTINGS_ROOT_YES, SETTINGS_ROOT_NO, SETTINGS_LANGUAGE,
    TERMINAL_BACK, TERMINAL_SCOPED, TERMINAL_PLACEHOLDER, TERMINAL_SEND,
    PACKAGES_TITLE, PACKAGES_MSG
}

fun t(lang: Lang, key: Key): String = when (lang) {
    Lang.EN -> when (key) {
        Key.TAB_PROCESSES -> "Processes"
        Key.TAB_PACKAGES -> "Packages"
        Key.TAB_SETTINGS -> "Settings"
        Key.SOUND_ON -> "Sound: on"
        Key.SOUND_OFF -> "Sound: off"
        Key.HOME_TITLE -> "Processes"
        Key.HOME_ROOT_NOT_GRANTED -> "root not granted"
        Key.HOME_SCANNING -> "scanning..."
        Key.HOME_APPS_RUNNING -> "apps running"
        Key.HOME_ROOT_MSG -> "This app needs root access to list other apps' processes. Accept the popup from your superuser manager (Magisk/KernelSU) when it appears."
        Key.HOME_NO_PROCESSES -> "No processes found."
        Key.SETTINGS_TITLE -> "Settings"
        Key.SETTINGS_VERSION -> "sxisco - version 0.1"
        Key.SETTINGS_ROOT_YES -> "Root: granted"
        Key.SETTINGS_ROOT_NO -> "Root: not granted"
        Key.SETTINGS_LANGUAGE -> "Language"
        Key.TERMINAL_BACK -> "< back"
        Key.TERMINAL_SCOPED -> "scoped"
        Key.TERMINAL_PLACEHOLDER -> "root command..."
        Key.TERMINAL_SEND -> "Send"
        Key.PACKAGES_TITLE -> "Packages"
        Key.PACKAGES_MSG -> "Not connected to a real package repository yet. Coming in a future update."
    }
    Lang.ES -> when (key) {
        Key.TAB_PROCESSES -> "Procesos"
        Key.TAB_PACKAGES -> "Packages"
        Key.TAB_SETTINGS -> "Ajustes"
        Key.SOUND_ON -> "Sonido: on"
        Key.SOUND_OFF -> "Sonido: off"
        Key.HOME_TITLE -> "Procesos"
        Key.HOME_ROOT_NOT_GRANTED -> "root no concedido"
        Key.HOME_SCANNING -> "escaneando..."
        Key.HOME_APPS_RUNNING -> "apps en ejecucion"
        Key.HOME_ROOT_MSG -> "Esta app necesita acceso root para listar los procesos de otras apps. Acepta el popup de tu gestor de superusuario (Magisk/KernelSU) cuando aparezca."
        Key.HOME_NO_PROCESSES -> "No se encontraron procesos."
        Key.SETTINGS_TITLE -> "Ajustes"
        Key.SETTINGS_VERSION -> "sxisco - version 0.1"
        Key.SETTINGS_ROOT_YES -> "Root: concedido"
        Key.SETTINGS_ROOT_NO -> "Root: no concedido"
        Key.SETTINGS_LANGUAGE -> "Idioma"
        Key.TERMINAL_BACK -> "< volver"
        Key.TERMINAL_SCOPED -> "delimitado"
        Key.TERMINAL_PLACEHOLDER -> "comando root..."
        Key.TERMINAL_SEND -> "Enviar"
        Key.PACKAGES_TITLE -> "Packages"
        Key.PACKAGES_MSG -> "Aun no conectado a un repositorio de paquetes real. Llegara en una futura actualizacion."
    }
    Lang.PT -> when (key) {
        Key.TAB_PROCESSES -> "Processos"
        Key.TAB_PACKAGES -> "Packages"
        Key.TAB_SETTINGS -> "Ajustes"
        Key.SOUND_ON -> "Som: on"
        Key.SOUND_OFF -> "Som: off"
        Key.HOME_TITLE -> "Processos"
        Key.HOME_ROOT_NOT_GRANTED -> "root nao concedido"
        Key.HOME_SCANNING -> "escaneando..."
        Key.HOME_APPS_RUNNING -> "apps em execucao"
        Key.HOME_ROOT_MSG -> "Esse app precisa de acesso root pra listar os processos de outros apps. Aceita o popup do seu gerenciador de superusuario (Magisk/KernelSU) quando ele aparecer."
        Key.HOME_NO_PROCESSES -> "Nenhum processo encontrado."
        Key.SETTINGS_TITLE -> "Ajustes"
        Key.SETTINGS_VERSION -> "sxisco - versao 0.1"
        Key.SETTINGS_ROOT_YES -> "Root: concedido"
        Key.SETTINGS_ROOT_NO -> "Root: nao concedido"
        Key.SETTINGS_LANGUAGE -> "Idioma"
        Key.TERMINAL_BACK -> "< voltar"
        Key.TERMINAL_SCOPED -> "escopado"
        Key.TERMINAL_PLACEHOLDER -> "comando root..."
        Key.TERMINAL_SEND -> "Enviar"
        Key.PACKAGES_TITLE -> "Packages"
        Key.PACKAGES_MSG -> "Ainda nao conectado a um repositorio de pacotes de verdade. Chega numa proxima atualizacao."
    }
}
