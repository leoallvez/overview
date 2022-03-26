package io.github.leoallvez.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
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
            minimumFetchIntervalInSeconds = getTimeInterval()
        }
        _remoteConfig.setConfigSettingsAsync(configSettings)
        onCompleteListener()
    }

    private fun getTimeInterval(): Long = if(BuildConfig.DEBUG)
        MIN_TIME_INTERVAL
    else
        MAX_TIME_INTERVAL


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

    companion object {
        private const val MIN_TIME_INTERVAL = 0L
        private const val MAX_TIME_INTERVAL = 3600L
    }
}