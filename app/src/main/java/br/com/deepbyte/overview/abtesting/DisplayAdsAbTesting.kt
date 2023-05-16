package br.com.deepbyte.overview.abtesting

import io.github.leoallvez.firebase.RemoteConfigKey.DISPLAY_ADS_KEY
import io.github.leoallvez.firebase.RemoteSource

data class DisplayAdsAbTesting(
    private val _localPermission: Boolean,
    private val _remoteSource: RemoteSource
) : AbTesting<Boolean> {

    override fun execute(): Boolean {
        val remotePermission = _remoteSource.getBoolean(DISPLAY_ADS_KEY)
        return _localPermission && remotePermission
    }
}
