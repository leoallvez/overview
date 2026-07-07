package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.presentation.createMovieDetailsMock
import br.dev.singular.overview.presentation.createTvShowDetailsMock
import br.dev.singular.overview.presentation.model.MediaUiType
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaDetailsTest {

    @Test
    fun `MovieDetails toUi should map correctly`() {
        // arrange
        val domain = createMovieDetailsMock()

        // act
        val ui = domain.toUi(isLiked = true)

        // assert
        assertEquals(domain.id, ui.metadata.id)
        assertEquals(domain.title, ui.metadata.title)
        assertEquals(true, ui.metadata.isLiked)
        assertEquals(domain.overview, ui.metadata.synopsis)
        assertEquals("01/01/2020", ui.metadata.releaseDate)
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", ui.metadata.backdropURL)
        assertEquals("2h", ui.durationFormatted)
        assertEquals("Director 1, Director 2", ui.directors.joinToString())
        assertEquals(MediaUiType.MOVIE, ui.type)
    }

    @Test
    fun `MovieDetails toUi with different runtimes should format correctly`() {
        assertEquals("1h 30min", createMovieDetailsMock().copy(runtime = 90).toUi(true).durationFormatted)
        assertEquals("45min", createMovieDetailsMock().copy(runtime = 45).toUi(true).durationFormatted)
        assertEquals("", createMovieDetailsMock().copy(runtime = 0).toUi(true).durationFormatted)
    }

    @Test
    fun `TvShowDetails toUi should map correctly`() {
        // arrange
        val domain = createTvShowDetailsMock()

        // act
        val ui = domain.toUi(isLiked = false)

        // assert
        assertEquals(domain.id, ui.metadata.id)
        assertEquals(domain.name, ui.metadata.title)
        assertEquals(false, ui.metadata.isLiked)
        assertEquals(domain.overview, ui.metadata.synopsis)
        assertEquals("01/01/2020", ui.metadata.releaseDate)
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", ui.metadata.backdropURL)
        assertEquals(5, ui.numberOfSeasons)
        assertEquals(50, ui.numberOfEpisodes)
        assertEquals("45min", ui.runtimePerEpisode)
        assertEquals("", ui.creators.joinToString())
        assertEquals(MediaUiType.TV, ui.type)
    }

    @Test
    fun `TvShowDetails toUi with multiple runtimes should format with average`() {
        // arrange
        val domain = createTvShowDetailsMock().copy(
            episodeRuntime = listOf(40, 50)
        )

        // act
        val ui = domain.toUi(isLiked = false)

        // assert
        assertEquals("45min", ui.runtimePerEpisode)
    }

    @Test
    fun `TvShowDetails toUi with multiple creators should format correctly`() {
        // arrange
        val domain = createTvShowDetailsMock().copy(
            creators = listOf("Creator 1", "Creator 2")
        )

        // act
        val ui = domain.toUi(isLiked = false)

        // assert
        assertEquals("Creator 1, Creator 2", ui.creators.joinToString())
    }
}
