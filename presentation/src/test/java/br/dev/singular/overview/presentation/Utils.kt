package br.dev.singular.overview.presentation

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.GenreUiModel
import java.util.Date
import java.util.UUID

fun createCatalogMock() = Catalog(
    id = 1L,
    name = "Netflix",
    priority = 1,
    logoPath = "path/to/logo",
    display = true,
    lastUpdate = Date()
)

fun createGenreMock() = Genre(
    id = 1L,
    name = "Action"
)

fun createPersonMock() = Person(
    id = 1,
    job = "Actor",
    name = "Celeste Beaumont",
    birthday = "1982-01-01",
    deathDay = "2006-01-01",
    profilePath = "https://image.tmdb.org/t/p/original",
    biography = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    character = "Himself",
    placeOfBirth = "Modesto, California, USA",
    tvShows = listOf(),
    movies = listOf()
)

fun createCatalogUiModelMock() = CatalogUiModel(
    id = 1L,
    priority = 1,
    logoURL = "https://example.com/logo.png",
    name = "Netflix",
    previewDrawableRes = null,
    uiId = UUID.randomUUID().toString()
)

fun createGenreUiModelMock() = GenreUiModel(
    id = 1L,
    name = "Action"
)

fun createQueryUiStateMock() = br.dev.singular.overview.presentation.model.QueryUiState(
    key = "key",
    type = br.dev.singular.overview.presentation.model.MediaUiType.MOVIE,
    isLiked = true,
    query = "search",
    page = 2
)

fun createMediaMock() = br.dev.singular.overview.domain.model.Media(
    id = 1L,
    type = br.dev.singular.overview.domain.model.MediaType.MOVIE,
    title = "Title",
    isLiked = true,
    posterPath = "/poster",
    lastUpdate = Date()
)

fun createQueryStateMock() = br.dev.singular.overview.domain.model.QueryState(
    key = "key",
    type = br.dev.singular.overview.domain.model.MediaType.MOVIE,
    isLiked = true,
    query = "search",
    page = 2
)
