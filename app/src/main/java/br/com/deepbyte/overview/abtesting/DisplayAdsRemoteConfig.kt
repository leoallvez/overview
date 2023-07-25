package br.com.deepbyte.overview.abtesting

import io.github.leoallvez.firebase.RemoteConfigKey.DISPLAY_ADS_KEY
import io.github.leoallvez.firebase.RemoteSource

data class DisplayAdsRemoteConfig(
    private val _localPermission: Boolean,
    private val _remoteSource: RemoteSource
) : RemoteConfig<Boolean> {
    override fun execute(): Boolean {
        val remotePermission = _remoteSource.getBoolean(DISPLAY_ADS_KEY)
        return _localPermission && remotePermission
    }
}
