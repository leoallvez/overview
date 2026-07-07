package br.dev.singular.overview.data.util

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.CreditsDataModel
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaListDataModel
import br.dev.singular.overview.data.model.MovieDetailsDataModel
import br.dev.singular.overview.data.model.PersonDetailsDataModel
import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.data.model.TvShowDetailsDataModel
import br.dev.singular.overview.data.model.VideoDataModel

val fakeSuggestionModels = listOf(
    SuggestionDataModel(
        id = 1,
        order = 1,
        type = MediaDataType.TV,
        sourceKey = "suggestion_the_witcher",
        isActive = true
    ),
    SuggestionDataModel(
        id = 2,
        order = 2,
        type = MediaDataType.MOVIE,
        sourceKey = "suggestion_dark",
        isActive = true
    )
)

val fakeMediaDataModel = MediaDataModel(
    id = 1,
    name = "Test Name",
    title = "Test Title",
    posterPath = "/test.jpg",
    type = MediaDataType.MOVIE,
    isLiked = false
)

val fakePersonDetailsDataModel = PersonDetailsDataModel(
    id = 1,
    name = "Maria Oliveira"
)

val fakeMovieDetailsDataModel = MovieDetailsDataModel(
    id = 1,
    title = "I'm fake data class",
    originalTitle = "I'm really a fake data class",
    releaseDate = "12/12/2012",
    runtime = 90,
    posterPath = "/test.jpg",
    backdropPath = "/test.jpg",
    overview = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    genres = emptyList(),
    credits = CreditsDataModel(),
    videos = emptyList(),
    catalogs = emptyList(),
    similar = MediaListDataModel()
)

val fakeTvShowDetailsDataModel = TvShowDetailsDataModel(
    id = 1,
    name = "I'm fake data class",
    originalName = "I'm really a fake data class",
    episodeRuntime = listOf(90, 90, 90),
    numberOfSeasons = 4,
    firstAirDate = "12/12/2012",
    posterPath = "/test.jpg",
    backdropPath = "/test.jpg",
    overview = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    genres = emptyList(),
    credits = CreditsDataModel(),
    videos = emptyList(),
    catalogs = emptyList(),
    similar = MediaListDataModel()
)

val fakeCatalogDataModel = CatalogDataModel(
    id = 1,
    name = "My name",
    priority = 1,
    logoPath = "/test.jpg",
    display = true,
)

fun createFakeCatalogDataModelList(count: Int): List<CatalogDataModel> {
    val list = mutableListOf<CatalogDataModel>()
    repeat(count) {
        list.add(fakeCatalogDataModel)
    }
    return list
}

val fakeVideoDataModel = VideoDataModel(
    id = "1",
    name = "Video",
    key = "key",
)

fun createFakeVideoDataModelList(count: Int): List<VideoDataModel> {
    val list = mutableListOf<VideoDataModel>()
    repeat(count) {
        list.add(fakeVideoDataModel)
    }
    return list
}
