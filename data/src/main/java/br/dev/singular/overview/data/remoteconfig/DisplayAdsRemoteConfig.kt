package br.dev.singular.overview.data.remoteconfig

import br.dev.singular.overview.data.remoteconfig.RemoteConfigKey.DISPLAY_ADS_KEY

data class DisplayAdsRemoteConfig(
    private val _localPermission: Boolean,
    private val _remoteSource: IRemoteConfigProvider
) : RemoteConfig<Boolean> {
    override fun execute(): Boolean {
        val remotePermission = _remoteSource.getBoolean(DISPLAY_ADS_KEY)
        return _localPermission || remotePermission
    }
}
