package br.dev.singular.overview.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavOptionsBuilder
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
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

fun createQueryUiStateMock() = QueryUiState(
    key = "key",
    type = br.dev.singular.overview.presentation.model.MediaUiType.MOVIE,
    isLiked = true,
    query = "search",
    page = 2
)

fun createMediaMock() = Media(
    id = 1L,
    type = MediaType.MOVIE,
    title = "Title",
    isLiked = true,
    posterPath = "/poster",
    lastUpdate = Date()
)

fun createQueryStateMock() = QueryState(
    key = "key",
    type = MediaType.MOVIE,
    isLiked = true,
    query = "search",
    page = 2
)

class NavigationWrapperMock: INavigationWrapper {

    var wasNavigateCalled = false

    override var activeRoute: String? = null

    override val startDestinationId  = 0

    var lastNavOptionsBuilder: (NavOptionsBuilder.() -> Unit)? = null

    override fun navigate(
        route: String,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        activeRoute = route
        wasNavigateCalled = true
        lastNavOptionsBuilder = builder
    }

    override fun toHome() {}
    override fun popBackStack() {}
    override fun toMediaDetails(media: MediaUiModel) {}

    @Composable
    override fun getCurrentRoute() = activeRoute
}