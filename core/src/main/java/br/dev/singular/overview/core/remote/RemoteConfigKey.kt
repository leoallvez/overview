package br.dev.singular.overview.core.remote

import br.dev.singular.overview.core.remote.RemoteConfigKey.STREAM_BR
import br.dev.singular.overview.core.remote.RemoteConfigKey.STREAM_ES
import br.dev.singular.overview.core.remote.RemoteConfigKey.STREAM_US

private const val STREAM_PREFIX = "stream"

enum class RemoteConfigKey(val value: String) {
    SUGGESTIONS_KEY("suggestions_v1"),
    DISPLAY_ADS_KEY("display_ads"),
    DISPLAY_HIGHLIGHT_ICONS_KEY("display_highlight_icons"),
    STREAM_BR("${STREAM_PREFIX}_BR"),
    STREAM_US("${STREAM_PREFIX}_US"),
    STREAM_ES("${STREAM_PREFIX}_ES"),
    FIREBASE_ENVIRONMENT_KEY("firebase_environment");

    companion object {
        private val streamingMap = entries.associateBy { it.value }

        fun getStreamingKeyByRegion(region: String): RemoteConfigKey {
            val targetValue = "${STREAM_PREFIX}_${region.uppercase()}"
            return streamingMap[targetValue] ?: STREAM_US
        }
    }
}
