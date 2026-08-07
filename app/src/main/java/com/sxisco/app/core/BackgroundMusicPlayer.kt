package com.sxisco.app.core

import android.content.Context
import android.media.MediaPlayer

class BackgroundMusicPlayer(
    private val context: Context,
    private val rawResId: Int
) {
    private var player: MediaPlayer? = null
    var isMuted: Boolean = true
        private set

    fun start() {
        if (player != null) return
        val mp = MediaPlayer.create(context, rawResId) ?: return
        mp.isLooping = true
        mp.setVolume(0f, 0f)
        mp.start()
        player = mp
        isMuted = true
    }

    fun toggleMute() {
        val mp = player ?: return
        isMuted = !isMuted
        val level = if (isMuted) 0f else 1f
        mp.setVolume(level, level)
    }

    fun release() {
        player?.release()
        player = null
    }
}
