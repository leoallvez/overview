package io.github.leoallvez.firebase

import io.github.leoallvez.firebase.RemoteConfigKey.STREAM_BR
import io.github.leoallvez.firebase.RemoteConfigKey.STREAM_ES
import io.github.leoallvez.firebase.RemoteConfigKey.STREAM_US

private const val STREAM_PREFIX = "stream"

enum class RemoteConfigKey(val value: String) {
    DISPLAY_ADS_KEY(value = "display_ads"),
    STREAM_BR(value = "${STREAM_PREFIX}_BR"),
    STREAM_US(value = "${STREAM_PREFIX}_US"),
    STREAM_ES(value = "${STREAM_PREFIX}_ES"),
    FIREBASE_ENVIRONMENT_KEY(value = "firebase_environment")
}

fun getStreamingKeyByRegion(region: String): RemoteConfigKey {
    return when ("${STREAM_PREFIX}_$region") {
        STREAM_BR.value -> STREAM_BR
        STREAM_ES.value -> STREAM_ES
        else -> STREAM_US
    }
}
