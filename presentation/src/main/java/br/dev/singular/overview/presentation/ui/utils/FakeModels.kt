package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.CreditsUiModel
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.model.MediaExtrasUiModel
import br.dev.singular.overview.presentation.model.MediaMetadataUiModel
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.PersonDetailsUiModel
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.model.VideoUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.UUID

@Composable
internal fun fakeMedias(
    count: Int = 20,
    withLongText: Boolean = false
): ImmutableList<MediaUiModel> {

    val baseModel = MediaUiModel(
        id = 0,
        title = stringResource(R.string.fake_short_title),
        posterURL = "https://imagens.com/movie.jpg",
        type = MediaUiType.MOVIE,
        previewDrawableRes = R.drawable.sample_poster
    )

    return buildList {
        repeat(count) { index ->
            val title = if (index % 2 == 1 || withLongText) {
                stringResource(R.string.fake_short_title)
            } else {
                "${stringResource(R.string.fake_long_title)} ${index + 1}"
            }
            add(
                baseModel.copy(
                    id = index.toLong(),
                    title = title,
                    uiId = UUID.randomUUID().toString()
                )
            )
        }
    }.toImmutableList()
}

internal fun fakeMedia() = MediaUiModel(
    id = 1,
    title = "Batman",
    type = MediaUiType.MOVIE,
    posterURL = "",
    previewDrawableRes = null
)

@Composable
internal fun fakeCatalogs(count: Int = 10): ImmutableList<CatalogUiModel> {

    val baseModel = CatalogUiModel(
        id = 0,
        priority = 1,
        name = stringResource(R.string.lorem_ipsum_long),
        logoURL = "https://imagens.com/catalog.jpg",
        previewDrawableRes = R.drawable.scifi_stream
    )

    return buildList {
        repeat(count) { index ->
            add(
                baseModel.copy(
                    id = index.toLong(),
                    uiId = UUID.randomUUID().toString()
                )
            )
        }
    }.toImmutableList()
}

internal fun fakeCatalogs() = CatalogUiModel(
    id = 1,
    priority = 1,
    logoURL = "",
    name = "Test Catalog",
    previewDrawableRes = R.drawable.scifi_stream
)

@Composable
internal fun fakeGenres(count: Int = 27): ImmutableList<GenreUiModel> {
    val genres = listOf(
        12L to "Adventure",
        14L to "Fantasy",
        16L to "Animation",
        18L to "Drama",
        27L to "Horror",
        28L to "Action",
        35L to "Comedy",
        36L to "History",
        37L to "Western",
        53L to "Thriller",
        80L to "Crime",
        99L to "Documentary",
        878L to "Science Fiction",
        9648L to "Mystery",
        10402L to "Music",
        10749L to "Romance",
        10751L to "Family",
        10752L to "War",
        10759L to "Action & Adventure",
        10762L to "Kids",
        10763L to "News",
        10764L to "Reality Show",
        10765L to "Sci-Fi & Fantasy",
        10766L to "Soap",
        10767L to "Talk",
        10768L to "War & Politics",
        10770L to "TV Movie"
    )

    return buildList {
        repeat(count) { index ->
            val (id, name) = genres.getOrElse(index % genres.size) { 0L to "Unknown" }
            add(GenreUiModel(id = id, name = name))
        }
    }.toImmutableList()
}


internal fun fakeQueryState() = QueryUiState(
    catalog = CatalogUiModel(
        id = 0,
        priority = 1,
        name = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        logoURL = "https://imagens.com/catalog.jpg",
        previewDrawableRes = R.drawable.scifi_stream
    )
)

@Composable
fun fakePersonDetails(): PersonDetailsUiModel {
    val fakeMedias = fakeMedias()
    return PersonDetailsUiModel(
        id = 0,
        job = "Actor",
        age = "24",
        name = "Celeste Beaumont",
        birthday = "01/01/1982",
        deathDay = "01/01/2006",
        biography = stringResource(R.string.lorem_ipsum_long),
        character = "Himself",
        profileURL = "",
        previewDrawableRes = R.drawable.sample_profile,
        placeOfBirth = "Modesto, California, USA",
        tvShows = fakeMedias,
        movies = fakeMedias
    )
}

@Composable
internal fun fakeMediaMetadata() = MediaMetadataUiModel(
    id = 1,
    title = stringResource(R.string.lorem_ipsum_short),
    isLiked = true,
    synopsis = stringResource(R.string.lorem_ipsum_long),
    isReleased = true,
    releaseDate = "12/12/2014",
    backdropURL = "https://image.jpg",
    posterPath = "",
    previewDrawableRes = R.drawable.sample_thumbnail
)

@Composable
internal fun fakeMovieDetails() = MediaDetailsUiModel.Movie(
    metadata = fakeMediaMetadata(),
    credits = CreditsUiModel(
        cast = fakePeople(),
        crew = fakePeople(),
    ),
    extras = MediaExtrasUiModel(
        videos = fakeVideos(),
        genres = fakeGenres(),
        similar = fakeMedias(),
        catalogs = fakeCatalogs(20)
    ),
    durationFormatted = "2h 49min",
    directors = listOf("Director 1", "Director 2").toImmutableList()
)

@Composable
internal fun fakeTvShowDetails() = MediaDetailsUiModel.TvShow(
    metadata = fakeMediaMetadata(),
    credits = CreditsUiModel(
        cast = fakePeople(),
        crew = fakePeople(),
    ),
    extras = MediaExtrasUiModel(
        videos = fakeVideos(),
        genres = fakeGenres(),
        similar = fakeMedias(),
        catalogs = fakeCatalogs(20)
    ),
    numberOfSeasons = 5,
    numberOfEpisodes = 62,
    creators = listOf("Creator 1", "Creator 2").toImmutableList(),
    runtimePerEpisode = "49min"
)

@Composable
internal fun fakePeople(count: Int = 10): ImmutableList<PersonUiModel> {
    return buildList {
        repeat(count) { index ->
            val loremIpsum = if (index % 2 == 1) {
                stringResource(R.string.lorem_ipsum_long)
            } else {
                stringResource(R.string.lorem_ipsum_short)
            }
            add(
                fakePerson().copy(
                    id = index.toLong(),
                    name = loremIpsum,
                    description = loremIpsum
                )
            )
        }
    }.toImmutableList()
}

internal fun fakePerson(
    name: String = "Cillian Murphy",
    description: String = "Thomas Shelby"
) = PersonUiModel(
    id = 1,
    name = name,
    description = description,
    profileURL = "",
    previewDrawableRes = R.drawable.sample_profile
)

@Composable
internal fun fakeVideos(count: Int = 10): ImmutableList<VideoUiModel> {
    val baseModel = fakeVideo(
        name = stringResource(R.string.lorem_ipsum_long)
    )

    return buildList {
        repeat(count) { index ->
            add(
                baseModel.copy(
                    id = index.toString(),
                    name = baseModel.name,
                    uiId = UUID.randomUUID().toString()
                )
            )
        }
    }.toImmutableList()
}

internal fun fakeVideo(name: String = "Sample Video") = VideoUiModel(
    id = "1",
    name = name,
    key = "key",
    thumbnailURL = "",
    previewDrawableRes = R.drawable.sample_thumbnail
)
