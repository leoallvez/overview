package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.Date

class QueryStateTest {

    @Test
    fun `QueryState toData should map all fields correctly`() {
        // arrange
        val domainState = QueryState(
            key = "tv/top_rated",
            type = MediaType.TV,
            isLiked = false,
            query = "drama",
            page = 2,
            genre = Genre(id = 18L, name = "Drama"),
            catalog = Catalog(
                id = 9L,
                name = "Amazon",
                priority = 1,
                logoPath = "/amazon.png",
                display = true,
                lastUpdate = Date()
            )
        )

        // act
        val dataState = domainState.toData()

        // assert
        assertEquals(domainState.key, dataState.path)
        assertEquals(MediaDataType.TV, dataState.type)
        assertEquals(domainState.isLiked, dataState.isLiked)
        assertEquals(domainState.query, dataState.query)
        assertEquals(domainState.page, dataState.page)
        assertNotNull(dataState.genre)
        assertEquals(domainState.genre?.id, dataState.genre?.id)
        assertNotNull(dataState.catalog)
        assertEquals(domainState.catalog?.id, dataState.catalog?.id)
    }
}
