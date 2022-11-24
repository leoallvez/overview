package br.com.deepbyte.overview.abtesting

import io.github.leoallvez.firebase.RemoteConfigKey
import io.github.leoallvez.firebase.RemoteSource

class DisplayAdsAbTesting(
    private val _localPermission: Boolean,
    private val _remoteSource: RemoteSource
) : AbTesting<Boolean> {

    override fun execute(): Boolean {
        val remotePermission = _remoteSource.getBoolean(RemoteConfigKey.DISPLAY_ADS_KEY)
        return _localPermission && remotePermission
    }
}
