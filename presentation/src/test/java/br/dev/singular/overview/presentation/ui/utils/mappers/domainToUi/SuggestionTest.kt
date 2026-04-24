package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.presentation.createMediaMock
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class SuggestionTest {

    @Test
    fun `List of Suggestion toUi should map correctly to a map of key and media list`() {
        // arrange
        val media = createMediaMock()
        val suggestions = listOf(
            Suggestion(
                id = 1L,
                key = "trending",
                order = 1,
                type = MediaType.MOVIE,
                isActive = true,
                medias = listOf(media),
                lastUpdate = Date()
            )
        )

        // act
        val ui = suggestions.toUi()

        // assert
        assertEquals(1, ui.size)
        assertEquals(1, ui["trending"]?.size)
        assertEquals(media.id, ui["trending"]?.get(0)?.id)
    }
}
