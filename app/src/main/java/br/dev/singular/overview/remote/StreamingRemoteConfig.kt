package br.dev.singular.overview.remote

import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.util.IJsonFileReader
import br.dev.singular.overview.util.parseToList
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.firebase.getStreamingKeyByRegion

class StreamingRemoteConfig(
    private val _region: String,
    private val _remoteSource: RemoteSource,
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
