package br.dev.singular.overview.core.remote

import br.dev.singular.overview.core.BuildConfig.REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS
import br.dev.singular.overview.core.remote.RemoteConfigKey.FIREBASE_ENVIRONMENT_KEY
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import timber.log.Timber

interface RemoteConfigProvider {
    fun getString(key: RemoteConfigKey): String
    fun getBoolean(key: RemoteConfigKey): Boolean
    fun start()
}

class RemoteConfigWrapper(
    private val remote: FirebaseRemoteConfig
) : RemoteConfigProvider {

    override fun getString(key: RemoteConfigKey) = remote.getString(key.value)

    override fun getBoolean(key: RemoteConfigKey) = remote.getBoolean(key.value)

    override fun start() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS
        }
        remote.setConfigSettingsAsync(configSettings)
        onCompleteListener()
    }

    private fun onCompleteListener() = with(remote) {
        fetch().addOnCompleteListener { task ->
            with(task) {
                if (isSuccessful) {
                    activate()
                }
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
