package io.github.leoallvez.take.abtest

import io.github.leoallvez.firebase.RemoteConfigKey.*
import io.github.leoallvez.firebase.RemoteSource

class DisplayAdsAbTest(
    private val localPermission: Boolean,
    private val remoteSource: RemoteSource,
) : AbTest<Boolean> {

    override fun execute(): Boolean {
        val remotePermission = remoteSource.getBoolean(DISPLAY_ADS_KEY)
        return localPermission && remotePermission
    }
}
