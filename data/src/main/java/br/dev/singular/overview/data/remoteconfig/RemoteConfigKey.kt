package br.dev.singular.overview.data.remoteconfig

enum class RemoteConfigKey(val value: String) {
    SUGGESTIONS_KEY("suggestions_v1"),
    DISPLAY_ADS_KEY("display_ads"),
    DISPLAY_AI_PREDICTION_KEY("display_ai_prediction"),
    STREAM_BR("stream_BR"),
    STREAM_US("stream_US"),
    STREAM_ES("stream_ES"),
    FIREBASE_ENVIRONMENT_KEY("firebase_environment");

    companion object {
        private val keysByValue = entries.associateBy { it.value }

        fun getKeyByRegion(region: String): RemoteConfigKey {
            val targetValue = "stream_${region.uppercase()}"
            return keysByValue[targetValue] ?: STREAM_US
        }
    }
}
