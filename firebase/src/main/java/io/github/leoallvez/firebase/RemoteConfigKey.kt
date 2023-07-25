package io.github.leoallvez.firebase

enum class RemoteConfigKey(val value: String) {
    DISPLAY_ADS_KEY(value = "display_ads"),
    SUGGESTIONS_LIST_KEY(value = "suggestions_list"),
    STREAMING_LIST_KEY(value = "streaming_list"),
    REMOTE_PROVIDERS_LIST_KEY(value = "remote_providers_list"),
    FIREBASE_ENVIRONMENT_KEY(value = "firebase_environment")
}
