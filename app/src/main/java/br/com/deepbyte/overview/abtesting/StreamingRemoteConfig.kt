package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.model.provider.StreamingConfig
import br.com.deepbyte.overview.util.fromJson
import io.github.leoallvez.firebase.RemoteConfigKey.REMOTE_PROVIDERS_LIST_KEY
import io.github.leoallvez.firebase.RemoteSource

class StreamingRemoteConfig(
    private val _remoteSource: RemoteSource
) : RemoteConfig<StreamingConfig> {

    override fun execute(): StreamingConfig {
        return getRemoteStreamings() ?: StreamingConfig()
    }

    private fun getRemoteStreamings(): StreamingConfig? =
        _remoteSource.getString(REMOTE_PROVIDERS_LIST_KEY).fromJson()
}
