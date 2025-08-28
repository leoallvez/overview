package br.dev.singular.overview.data.util

import br.dev.singular.overview.data.local.source.IMediaRouteLocalDataSource
import br.dev.singular.overview.data.model.MediaRouteDataModel

class MockMediaRouteLocalDataSource : IMediaRouteLocalDataSource {
    override suspend fun getByKey(key: String): MediaRouteDataModel? {
        return listOf(
            MediaRouteDataModel("all_trending", "trending/all/day"),
        ).find { it.key == key }
    }
}
