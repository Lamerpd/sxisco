package com.sxisco.app.data

data class RunningProcess(
    val pid: Int,
    val uid: Int,
    val packageName: String,
    val label: String
)
