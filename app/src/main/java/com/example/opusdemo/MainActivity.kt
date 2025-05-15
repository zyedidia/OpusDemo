package com.example.opusdemo

import android.net.Uri
import android.os.Bundle
import android.view.TextureView
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView


class MainActivity : ComponentActivity() {
    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) Build a DefaultRenderersFactory configured to prefer the FFmpeg extension
        val renderersFactory = DefaultRenderersFactory(this).apply {
            // PREFER will try FFmpeg first, then fall back to MediaCodec
            setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)    // To *force* FFmpeg only and never use MediaCodec:
            // setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
            // and then manually add FfmpegAudioRenderer in createAudioRenderers()
        }   // 2) Build ExoPlayer with that factory
        player = ExoPlayer.Builder(this, renderersFactory)
            .setTrackSelector(DefaultTrackSelector(this))
            .build()
        playerView = findViewById<PlayerView>(R.id.player_view)

        // Initialize ExoPlayer
        //player = ExoPlayer.Builder(this).build()
        playerView!!.player = player

        // Build the MediaItem
        val videoUrl =
            "android.resource://${packageName}/raw/rickroll"
        val uri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(uri)

        // Prepare the player with the media item
        player!!.setMediaItem(mediaItem)
        player!!.prepare()
        player!!.playWhenReady = true // Start playing when ready
    }

    override fun onStop() {
        super.onStop()
        if (player != null) {
            player!!.release()
            player = null
        }
    }
}