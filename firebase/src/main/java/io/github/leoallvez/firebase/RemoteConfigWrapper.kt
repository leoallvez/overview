package io.github.leoallvez.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import timber.log.Timber

class RemoteConfigWrapper(
    private val _remoteConfig: FirebaseRemoteConfig? = FirebaseRemoteConfig.getInstance()
) : RemoteSource {

    override fun getString(key: String): String {
        return _remoteConfig?.getString(key) ?: ""
    }

    override fun getBoolean(key: String): Boolean {
        return _remoteConfig?.getBoolean(key) ?: false
    }

    fun start() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = TIME_INTERVAL
        }
        _remoteConfig?.setConfigSettingsAsync(configSettings)
        onCompleteListener()
    }

    private fun onCompleteListener() {
        _remoteConfig?.let {
            it.fetch().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    it.activate()
                    Timber.d("Remote Config started")
                } else {
                    Timber.d("Remote Config not started")
                }
            }
        }
    }

    companion object {
        private val TIME_INTERVAL = if (BuildConfig.DEBUG) 0L else 3600L
    }
}