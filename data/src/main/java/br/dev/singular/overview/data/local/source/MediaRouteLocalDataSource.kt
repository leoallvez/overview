package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.model.MediaRouteDataModel
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface IMediaRouteLocalDataSource {
    suspend fun getByKey(key: String): MediaRouteDataModel?
}

class MediaRouteLocalDataSource @Inject constructor(
    private val readerProvider: IJsonFileReaderProvider
) : IMediaRouteLocalDataSource {

    override suspend fun getByKey(key: String) = loadRoutes().firstOrNull { it.key == key }

    private fun loadRoutes(): List<MediaRouteDataModel> {
        return routes.ifEmpty  {
            val json = readerProvider.read(MEDIA_ROUTES_FILE_NAME)
            Json.decodeFromString<List<MediaRouteDataModel>>(json).also { routes = it }
        }
    }

    companion object {
        private const val MEDIA_ROUTES_FILE_NAME = "media_routes.json"
        private var routes = emptyList<MediaRouteDataModel>()
    }
}
