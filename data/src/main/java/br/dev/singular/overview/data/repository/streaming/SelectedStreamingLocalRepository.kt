package br.dev.singular.overview.data.repository.streaming

import br.dev.singular.overview.data.local.source.CacheDataSource
import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Update
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SelectedStreamingLocalRepository @Inject constructor(
    private val json: Json,
    private val dataSource: CacheDataSource,
    private val readerProvider: IJsonFileReaderProvider,
) : Get<Streaming?>, Update<Streaming> {

    override suspend fun get(): Streaming? {

        val jsonString = dataSource.getValue(KEY_CACHE).first()
            ?: readerProvider.read(filePath = DEFAULT_STREAMING)

        return runCatching {
            json.decodeFromString<StreamingDataModel>(jsonString).toDomain()
        }.getOrNull()
    }

    override suspend fun update(item: Streaming) {
        val jsonString = json.encodeToString(item.toData())
        dataSource.setValue(KEY_CACHE, jsonString)
    }

    private companion object {
        val KEY_CACHE = CacheDataSource.Companion.KEY_SELECTED_STREAMING_CACHE
        const val DEFAULT_STREAMING = "default_streaming.json"
    }
}
