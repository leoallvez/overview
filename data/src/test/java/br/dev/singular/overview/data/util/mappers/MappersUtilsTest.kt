package br.dev.singular.overview.data.util.mappers

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.QueryDataState
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class MappersUtilsTest {

    @Test
    fun `toParams should merge extraParams with genre and catalog ids`() {
        // arrange
        val queryDataState = QueryDataState(
            path = "movie/popular",
            genre = GenreDataModel(id = 10, name = "Action"),
            catalog = CatalogDataModel(id = 20, name = "Netflix", priority = 1, logoPath = "", display = true)
        )
        val extraParams = mapOf("api_key" to "123")

        // act
        val result = queryDataState.toParams(extraParams)

        // assert
        result["api_key"] shouldBeEqualTo "123"
        result["with_genres"] shouldBeEqualTo "10"
        result["with_watch_providers"] shouldBeEqualTo "20"
    }

    @Test
    fun `toParams should return only extraParams when genre and catalog are null`() {
        // arrange
        val queryDataState = QueryDataState(path = "")
        val extraParams = mapOf("api_key" to "123")

        // act
        val result = queryDataState.toParams(extraParams)

        // assert
        result shouldBeEqualTo extraParams
    }
}
