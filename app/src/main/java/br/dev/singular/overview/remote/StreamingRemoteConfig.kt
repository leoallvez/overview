package br.dev.singular.overview.remote

import br.dev.singular.overview.core.remote.RemoteConfigProvider
import br.dev.singular.overview.core.remote.getStreamingKeyByRegion
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.util.IJsonFileReader
import br.dev.singular.overview.util.parseToList

class StreamingRemoteConfig(
    private val _region: String,
    private val _remoteSource: RemoteConfigProvider,
    private val _jsonFileReader: IJsonFileReader
) : RemoteConfig<List<StreamingEntity>> {

    override fun execute() = getRemoteStreaming().ifEmpty { getLocalStreaming() }

    private fun getLocalStreaming(): List<StreamingEntity> {
        val json = _jsonFileReader.read(getStreamingFileName())
        return json.parseToList()
    }

    private fun getStreamingFileName() = "streams/$_region.json"

    private fun getRemoteStreaming(): List<StreamingEntity> {
        val json = _remoteSource.getString(getStreamingRemoteKey())
        return json.parseToList()
    }

    private fun getStreamingRemoteKey() = getStreamingKeyByRegion(_region)
}
