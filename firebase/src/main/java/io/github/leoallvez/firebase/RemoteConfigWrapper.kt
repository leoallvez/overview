package io.github.leoallvez.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import timber.log.Timber

class RemoteConfigWrapper(
    private val _remoteConfig: FirebaseRemoteConfig
) : RemoteSource {

    init {
        start()
    }

    override fun getString(key: RemoteConfigKey): String {
        return _remoteConfig.getString(key.value)
    }

    override fun getBoolean(key: RemoteConfigKey): Boolean {
        return _remoteConfig.getBoolean(key.value)
    }

    private fun start() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = TIME_INTERVAL
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
                Timber.i("Remote Config not started")
            }
        }
    }

    companion object {
        private const val EMPTY = ""
        private const val TIME_INTERVAL = 3600L
    }
}