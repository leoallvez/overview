package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.StreamingOptions
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.fromJson
import io.github.leoallvez.firebase.RemoteConfigKey.STREAMINGS_BY_REGIONS_KEY
import io.github.leoallvez.firebase.RemoteSource

class StreamingOptionsRemoteConfig(
    private val _jsonFileReader: IJsonFileReader,
    private val _remoteSource: RemoteSource
) : RemoteConfig<StreamingOptions> {

    override fun execute(): StreamingOptions {
        return getRemoteStreamingOptions() ?: getLocalStreamingOptions()
    }

    private fun getLocalStreamingOptions(): StreamingOptions {
        val json = _jsonFileReader.read(STREAMINGS_FILE_NAME)
        return json.fromJson() ?: StreamingOptions()
    }

    private fun getRemoteStreamingOptions(): StreamingOptions? {
        val json = _remoteSource.getString(STREAMINGS_BY_REGIONS_KEY)
        return json.fromJson()
    }

    companion object {
        private const val STREAMINGS_FILE_NAME = "streamings.json"
    }
}