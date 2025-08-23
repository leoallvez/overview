package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.model.MediaRouteDataModel
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject


interface IMediaRouteLocalDataSource {
    suspend fun getByKey(key: String): MediaRouteDataModel?
}

class MediaRouteLocalDataSource @Inject constructor(
    private val jsonFileReaderProvider: IJsonFileReaderProvider
) : IMediaRouteLocalDataSource {

    override suspend fun getByKey(key: String): MediaRouteDataModel? {
        return loadRoutes().firstOrNull { it.key == key }
    }

    private fun loadRoutes(): List<MediaRouteDataModel> {
        return routes.ifEmpty  {
            val json = jsonFileReaderProvider.read(MEDIA_ROUTES_FILE_NAME)
            Json.decodeFromString<List<MediaRouteDataModel>>(json).also { routes = it }
        }
    }

    private companion object {
        const val MEDIA_ROUTES_FILE_NAME = "media_routes.json"
        var routes = emptyList<MediaRouteDataModel>()
    }
}
