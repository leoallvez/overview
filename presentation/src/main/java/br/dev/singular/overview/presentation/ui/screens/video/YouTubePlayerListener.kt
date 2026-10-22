package br.dev.singular.overview.presentation.ui.screens.video

import br.dev.singular.overview.presentation.ui.screens.video.interaction.YouTubePlayerActions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class YouTubePlayerListener(
    private val videoKey: String,
    private val actions: YouTubePlayerActions
) : AbstractYouTubePlayerListener() {

    override fun onReady(youTubePlayer: YouTubePlayer) {
        super.onReady(youTubePlayer)
        youTubePlayer.cueVideo(videoId = videoKey, startSeconds = 0f)
    }

    override fun onStateChange(
        youTubePlayer: YouTubePlayer,
        state: PlayerConstants.PlayerState
    ) {
        actions.onPlayerStateChange(state.name.lowercase())
    }
}
