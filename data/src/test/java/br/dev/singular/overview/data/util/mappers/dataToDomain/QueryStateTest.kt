package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class QueryStateTest {

    @Test
    fun `QueryDataState toDomain should map all fields correctly`() {
        // arrange
        val dataState = QueryDataState(
            path = "movie/popular",
            type = MediaDataType.MOVIE,
            isLiked = true,
            query = "action",
            page = 1,
            genre = GenreDataModel(id = 28L, name = "Action"),
            catalog = CatalogDataModel(id = 8L, name = "Netflix")
        )

        // act
        val domainState = dataState.toDomain()

        // assert
        assertEquals(dataState.path, domainState.key)
        assertEquals(MediaType.MOVIE, domainState.type)
        assertEquals(dataState.isLiked, domainState.isLiked)
        assertEquals(dataState.query, domainState.query)
        assertEquals(dataState.page, domainState.page)
        assertNotNull(domainState.genre)
        assertEquals(dataState.genre?.id, domainState.genre?.id)
        assertNotNull(domainState.catalog)
        assertEquals(dataState.catalog?.id, domainState.catalog?.id)
    }
}
