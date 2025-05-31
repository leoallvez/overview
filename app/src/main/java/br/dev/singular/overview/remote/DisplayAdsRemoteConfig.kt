package br.dev.singular.overview.remote

import br.dev.singular.overview.core.remote.RemoteConfigKey.DISPLAY_ADS_KEY
import br.dev.singular.overview.core.remote.RemoteConfigProvider

data class DisplayAdsRemoteConfig(
    private val _localPermission: Boolean,
    private val _remoteSource: RemoteConfigProvider
) : RemoteConfig<Boolean> {
    override fun execute(): Boolean {
        val remotePermission = _remoteSource.getBoolean(DISPLAY_ADS_KEY)
        return _localPermission || remotePermission
    }
}
