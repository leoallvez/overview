package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IGenreLocalDataSource
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.IVideoRemoteDataSource
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.Video
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal data class MediaDetailsExtras(
    val videos: List<Video>,
    val catalogs: List<Catalog>,
    val genres: List<Genre>,
)

class MediaDetailsExtrasRepository @Inject constructor(
    private val videoDataSource: IVideoRemoteDataSource,
    private val catalogDataSource: ICatalogRemoteDataSource,
    private val genreDataSource: IGenreLocalDataSource,
) {

    internal suspend fun fetch(
        id: Long,
        type: MediaDataType,
        genres: List<Genre>
    ): MediaDetailsExtras = coroutineScope {
        val videosDeferred = async { videoDataSource.fetchVideos(id, type) }
        val catalogsDeferred = async { catalogDataSource.fetchCatalogs(id, type) }
        val genresDeferred = async { genreDataSource.fillOriginalNames(genres) }

        MediaDetailsExtras(
            videos = videosDeferred.await(),
            catalogs = catalogsDeferred.await(),
            genres = genresDeferred.await(),
        )
    }
}
