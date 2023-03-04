package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.parseToList
import io.github.leoallvez.firebase.RemoteConfigKey.STREAMING_LIST_KEY
import io.github.leoallvez.firebase.RemoteSource

class StreamingAbTesting(
    private val _jsonFileReader: IJsonFileReader,
    private val _remoteSource: RemoteSource
) : AbTesting<List<Streaming>> {

    override fun execute() = getRemoteStreamings()
        .ifEmpty { getLocalStreamings() }

    private fun getLocalStreamings(): List<Streaming> {
        val json = _jsonFileReader.read(STREAMING_FILE_NAME)
        return json.toStreamings()
    }

    private fun getRemoteStreamings(): List<Streaming> {
        val json = _remoteSource.getString(STREAMING_LIST_KEY)
        return json.toStreamings()
    }

    private fun String.toStreamings() = this.parseToList<Streaming>()

    companion object {
        private const val STREAMING_FILE_NAME = "streamings.json"
    }
}
