package br.dev.singular.overview.util

import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagPlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class YouTubePlayerListener(private val videoKey: String) : AbstractYouTubePlayerListener() {

    override fun onReady(youTubePlayer: YouTubePlayer) {
        super.onReady(youTubePlayer)
        youTubePlayer.cueVideo(videoKey, startSeconds = 0f)
    }

    override fun onStateChange(
        youTubePlayer: YouTubePlayer,
        state: PlayerConstants.PlayerState
    ) {
        TagManager.logInteraction(TagPlayer.PATH, makeDetail(state.name.lowercase()))
    }

    private fun makeDetail(state: String) = "${TagPlayer.Detail.PLAYER_STATUS}$state"

}
