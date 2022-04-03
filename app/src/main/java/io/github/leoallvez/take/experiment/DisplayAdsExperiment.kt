package io.github.leoallvez.take.experiment

import io.github.leoallvez.firebase.RemoteConfigKey.*
import io.github.leoallvez.firebase.RemoteSource

//TODO: write unit test
class DisplayAdsExperiment(
    private val localPermission: Boolean,
    private val remoteSource: RemoteSource,
) : AbExperiment<Boolean> {

    override fun execute(): Boolean? {
        val remotePermission = remoteSource.getBoolean(DISPLAY_ADS_KEY)
        return localPermission && remotePermission
    }
}
