package br.dev.singular.overview.core.remote

private const val STREAM_PREFIX = "stream"

enum class RemoteConfigKey(val value: String) {
    SUGGESTIONS_KEY("suggestions_v1"),
    DISPLAY_ADS_KEY("display_ads"),
    STREAM_BR("${STREAM_PREFIX}_BR"),
    STREAM_US("${STREAM_PREFIX}_US"),
    STREAM_ES("${STREAM_PREFIX}_ES"),
    FIREBASE_ENVIRONMENT_KEY("firebase_environment");

    companion object {
        private val catalogMap = entries.associateBy { it.value }

        fun getCatalogKeyByRegion(region: String): RemoteConfigKey {
            val targetValue = "${STREAM_PREFIX}_${region.uppercase()}"
            return catalogMap[targetValue] ?: STREAM_US
        }
    }
}
