package br.dev.singular.overview.remote

import br.dev.singular.overview.core.remote.RemoteSource
import br.dev.singular.overview.core.remote.RemoteConfigKey.DISPLAY_ADS_KEY

data class DisplayAdsRemoteConfig(
    private val _localPermission: Boolean,
    private val _remoteSource: RemoteSource
) : RemoteConfig<Boolean> {
    override fun execute(): Boolean {
        val remotePermission = _remoteSource.getBoolean(DISPLAY_ADS_KEY)
        return _localPermission && remotePermission
    }
}
