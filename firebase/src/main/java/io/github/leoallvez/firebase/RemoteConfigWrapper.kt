package io.github.leoallvez.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import io.github.leoallvez.firebase.BuildConfig.REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS
import io.github.leoallvez.firebase.RemoteConfigKey.FIREBASE_ENVIRONMENT_KEY
import timber.log.Timber

interface RemoteSource {
    fun getString(key: RemoteConfigKey): String
    fun getBoolean(key: RemoteConfigKey): Boolean
    fun start()
}

class RemoteConfigWrapper(
    private val _remote: FirebaseRemoteConfig
) : RemoteSource {

    override fun getString(key: RemoteConfigKey) =
        _remote.getString(key.value)

    override fun getBoolean(key: RemoteConfigKey) =
        _remote.getBoolean(key.value)

    override fun start() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS
        }
        _remote.setConfigSettingsAsync(configSettings)
        onCompleteListener()
    }

    private fun onCompleteListener() = with(_remote) {
        fetch().addOnCompleteListener { task ->
            with(task) {
                if (isSuccessful) { activate() }
                log(isSuccessful)
            }
        }
    }

    private fun log(success: Boolean) {
        val message = if (success) {
            val environment = getString(key = FIREBASE_ENVIRONMENT_KEY)
            "started in environment: $environment"
        } else {
            "not started"
        }
        Timber.i(message = "Remote Config $message")
    }
}
