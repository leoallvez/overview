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
    fun `MediaDetailsUiModel toMediaDomain should map the original title and genres`() {
        // arrange
        val movie = createMovieDetailsMock().copy(
            genres = listOf(
                Genre(id = 1L, name = "Ação", originalName = "Action"),
                Genre(id = 2L, name = "Comédia", originalName = "")
            )
        )

        // act
        val domain = movie.toUi(isLiked = true).toMediaDomain()

        // assert
        assertEquals(movie.originalTitle, domain.originalTitle)
        assertEquals(listOf("Action", "Comédia"), domain.genres)
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
        assertEquals(MediaType.MOVIE, domain.type)
        assertEquals(movie.originalTitle, domain.title)
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
        assertEquals(MediaType.TV, domain.type)
        assertEquals(tvShow.originalName, domain.title)
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

    @Test
    fun `MediaDetailsUiModel toDomain should use the title when the original title is empty`() {
        // arrange
        val movie = createMovieDetailsMock().copy(originalTitle = "")

        // act
        val domain = movie.toUi(isLiked = false).toDomain()

        // assert
        assertEquals(movie.title, domain.title)
    }
}
