package io.github.leoallvez.firebase

enum class RemoteConfigKey(val value: String) {
    DISPLAY_ADS_KEY(value = "display_ads"),
    SUGGESTIONS_LIST_KEY(value = "suggestions_list"),
    STREAMINGS_BY_REGIONS_KEY(value = "streamings_by_regions"),
    FIREBASE_ENVIRONMENT_KEY(value = "firebase_environment")
}
