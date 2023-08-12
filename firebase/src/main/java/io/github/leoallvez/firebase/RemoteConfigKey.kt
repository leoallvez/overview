package io.github.leoallvez.firebase

import io.github.leoallvez.firebase.RemoteConfigKey.STREAMINGS_BR
import io.github.leoallvez.firebase.RemoteConfigKey.STREAMINGS_ES
import io.github.leoallvez.firebase.RemoteConfigKey.STREAMINGS_US

private const val STREAMINGS_PREFIX = "streamings"
enum class RemoteConfigKey(val value: String) {
    DISPLAY_ADS_KEY(value = "display_ads"),
    STREAMINGS_BR(value = "${STREAMINGS_PREFIX}_BR"),
    STREAMINGS_US(value = "${STREAMINGS_PREFIX}_US"),
    STREAMINGS_ES(value = "${STREAMINGS_PREFIX}_ES"),
    FIREBASE_ENVIRONMENT_KEY(value = "firebase_environment")
}

fun getStreamingKeyByRegion(region: String): RemoteConfigKey {
    return when ("${STREAMINGS_PREFIX}_$region") {
        STREAMINGS_BR.value -> STREAMINGS_BR
        STREAMINGS_ES.value -> STREAMINGS_ES
        else -> STREAMINGS_US
    }
}
