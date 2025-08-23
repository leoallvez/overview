package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.model.MediaRouteDataModel
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import kotlinx.serialization.json.Json
import javax.inject.Inject


interface IMediaRouteLocalDataSource {
    fun getByKey(key: String): MediaRouteDataModel?
}

class MediaRouteLocalDataSource @Inject constructor(
    private val jsonFileReaderProvider: IJsonFileReaderProvider
) : IMediaRouteLocalDataSource {

    override fun getByKey(key: String): MediaRouteDataModel? {
        return getAll().find { it.key == key }
    }

    private fun getAll(): List<MediaRouteDataModel> {
        return routes.ifEmpty {
            val json = jsonFileReaderProvider.read(MEDIA_ROUTES_FILE_NAME)
            Json.decodeFromString<List<MediaRouteDataModel>>(json).also { routes = it }
        }
    }

    private companion object {
        const val MEDIA_ROUTES_FILE_NAME = "media_routes.json"
        var routes = emptyList<MediaRouteDataModel>()
    }
}
