package br.dev.singular.overview.util

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import timber.log.Timber

class YouTubePlayerListener(private val videoKey: String) : AbstractYouTubePlayerListener() {

    override fun onReady(youTubePlayer: YouTubePlayer) {
        super.onReady(youTubePlayer)
        youTubePlayer.cueVideo(videoKey, startSeconds = 0f)
    }

    override fun onStateChange(
        youTubePlayer: YouTubePlayer,
        state: PlayerConstants.PlayerState
    ) {
        when (state) {
            PlayerConstants.PlayerState.PLAYING -> {
                Timber.d("video is playing.")
            }
            PlayerConstants.PlayerState.PAUSED -> {
                Timber.d("video is paused.")
            }
            PlayerConstants.PlayerState.ENDED -> {
                Timber.d("Video ended.")
            }
            else -> { Timber.d("Actual state: $state") }
        }
    }
}
