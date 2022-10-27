package io.github.leoallvez.take.abtest

import io.github.leoallvez.firebase.RemoteConfigKey
import io.github.leoallvez.firebase.RemoteSource

class DisplayAdsAbTest(
    private val _localPermission: Boolean,
    private val _remoteSource: RemoteSource
) : AbTest<Boolean> {

    override fun execute(): Boolean {
        val remotePermission = _remoteSource.getBoolean(RemoteConfigKey.DISPLAY_ADS_KEY)
        return _localPermission && remotePermission
    }
}
