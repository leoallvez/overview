package br.com.deepbyte.overview.remote

import br.com.deepbyte.overview.data.model.provider.StreamingEntity
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.parseToList
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.firebase.getStreamingKeyByRegion

class StreamingsRemoteConfig(
    private val _region: String,
    private val _remoteSource: RemoteSource,
    private val _jsonFileReader: IJsonFileReader
) : RemoteConfig<List<StreamingEntity>> {

    override fun execute() = getRemoteStreamings().ifEmpty { getLocalStreamings() }

    private fun getLocalStreamings(): List<StreamingEntity> {
        val json = _jsonFileReader.read(getStreamingFileName())
        return json.parseToList()
    }

    private fun getStreamingFileName() = "streamings/$_region.json"

    private fun getRemoteStreamings(): List<StreamingEntity> {
        val json = _remoteSource.getString(getStreamingRemoteKey())
        return json.parseToList()
    }

    private fun getStreamingRemoteKey() = getStreamingKeyByRegion(_region)
}
