package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.presentation.createMovieDetailsMock
import br.dev.singular.overview.presentation.createTvShowDetailsMock
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaDetailsTest {

    @Test
    fun `MediaDetailsUiModel toMediaDomain should map the metadata to media`() {
        // arrange
        val ui = createMovieDetailsMock().toUi(isLiked = true)

        // act
        val domain = ui.toMediaDomain()

        // assert
        assertEquals(ui.metadata.id, domain.id)
        assertEquals(MediaType.MOVIE, domain.type)
        assertEquals(ui.metadata.title, domain.title)
        assertEquals(true, domain.isLiked)
        assertEquals(ui.metadata.posterPath, domain.posterPath)
    }

    @Test
    fun `MediaDetailsUiModel toDomain should map a movie to prediction input`() {
        // arrange
        val movie = createMovieDetailsMock().copy(
            genres = listOf(Genre(id = 1L, name = "Ação", originalName = "Action"))
        )

        // act
        val domain = movie.toUi(isLiked = false).toDomain()

        // assert
        assertEquals(movie.id, domain.id)
        assertEquals(movie.title, domain.title)
        assertEquals(listOf("Action"), domain.genres)
    }

    @Test
    fun `MediaDetailsUiModel toDomain should map a tv show to prediction input`() {
        // arrange
        val tvShow = createTvShowDetailsMock().copy(
            genres = listOf(Genre(id = 2L, name = "Comédia", originalName = "Comedy"))
        )

        // act
        val domain = tvShow.toUi(isLiked = false).toDomain()

        // assert
        assertEquals(tvShow.id, domain.id)
        assertEquals(tvShow.name, domain.title)
        assertEquals(listOf("Comedy"), domain.genres)
    }

    @Test
    fun `MediaDetailsUiModel toDomain should use the translated name when the original is empty`() {
        // arrange
        val movie = createMovieDetailsMock().copy(
            genres = listOf(Genre(id = 1L, name = "Ação", originalName = ""))
        )

        // act
        val domain = movie.toUi(isLiked = false).toDomain()

        // assert
        assertEquals(listOf("Ação"), domain.genres)
    }
}
