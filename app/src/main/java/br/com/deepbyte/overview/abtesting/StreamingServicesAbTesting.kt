package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.parseToList
import io.github.leoallvez.firebase.RemoteConfigKey.STREAMING_LIST_KEY
import io.github.leoallvez.firebase.RemoteSource

class StreamingServicesAbTesting(
    private val _jsonFileReader: IJsonFileReader,
    private val _remoteSource: RemoteSource
) : AbTesting<List<Streaming>> {

    override fun execute() = getRemoteStreamingServices()
        .ifEmpty { getLocalStreamingServices() }

    private fun getLocalStreamingServices(): List<Streaming> {
        val json = _jsonFileReader.read(STREAMING_FILE_NAME)
        return json.toStreamingServices()
    }

    private fun getRemoteStreamingServices(): List<Streaming> {
        val json = _remoteSource.getString(STREAMING_LIST_KEY)
        return json.toStreamingServices()
    }

    private fun String.toStreamingServices() = parseToList<Streaming>()

    companion object {
        private const val STREAMING_FILE_NAME = "streaming_services.json"
    }
}
