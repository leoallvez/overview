package io.github.leoallvez.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import io.github.leoallvez.firebase.BuildConfig.REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS
import timber.log.Timber

class RemoteConfigWrapper(
    private val _remoteConfig: FirebaseRemoteConfig
) : RemoteSource {

    override fun getString(key: RemoteConfigKey): String {
        return _remoteConfig.getString(key.value)
    }

    override fun getBoolean(key: RemoteConfigKey): Boolean {
        return _remoteConfig.getBoolean(key.value)
    }

    fun start() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS
        }
        _remoteConfig.setConfigSettingsAsync(configSettings)
        onCompleteListener()
    }

    private fun onCompleteListener() = with(_remoteConfig) {
        fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                activate()
                Timber.i("Remote Config started")
            } else {
                Timber.d("Remote Config not started")
            }
        }
    }
}