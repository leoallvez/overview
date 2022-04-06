package io.github.leoallvez.take.experiment

import android.util.Log
import io.github.leoallvez.firebase.RemoteConfigKey.*
import io.github.leoallvez.firebase.RemoteSource

class DisplayAdsExperiment(
    private val localPermission: Boolean,
    private val remoteSource: RemoteSource,
) : AbExperiment<Boolean> {

    override fun execute(): Boolean {
        val remotePermission = remoteSource.getBoolean(DISPLAY_ADS_KEY)
        Log.i("ads_permissions",
            "local: $localPermission, remote: $remotePermission")
        return localPermission && remotePermission
    }
}
