package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.model.MediaRouteDataModel
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface IMediaRouteLocalDataSource {
    suspend fun getByKey(key: String): MediaRouteDataModel?
}

class MediaRouteLocalDataSource @Inject constructor(
    private val json: Json,
    private val readerProvider: IJsonFileReaderProvider
) : IMediaRouteLocalDataSource {

    override suspend fun getByKey(key: String) = loadRoutes().firstOrNull { it.key == key }

    private fun loadRoutes(): List<MediaRouteDataModel> {
        return routes.ifEmpty {
            val jsonString = readerProvider.read(filePath = MEDIA_ROUTES_FILE_NAME)
            if (jsonString.isBlank()) return emptyList()
            runCatching {
                json.decodeFromString<List<MediaRouteDataModel>>(jsonString)
            }.getOrDefault(emptyList()).also { routes = it }
        }
    }

    private companion object {
        const val MEDIA_ROUTES_FILE_NAME = "media_routes.json"
        var routes = emptyList<MediaRouteDataModel>()
    }
}
