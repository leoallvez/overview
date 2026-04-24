package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.model.MediaRouteDataModel
import br.dev.singular.overview.data.util.IJsonFileReaderProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaRouteLocalDataSourceTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val readerProvider: IJsonFileReaderProvider = mockk()
    private val sut = MediaRouteLocalDataSource(json, readerProvider)

    @Test
    fun `getByKey should return matching route from json file`() = runTest {
        // arrange
        val routes = listOf(MediaRouteDataModel(key = "popular", path = "movie/popular"))
        val routesJson = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(MediaRouteDataModel.serializer()), routes)
        
        every { readerProvider.read(any()) } returns routesJson

        // act
        val result = sut.getByKey("popular")

        // assert
        assertEquals(routes.first(), result)
    }

    @Test
    fun `getByKey should return null when key not found`() = runTest {
        // arrange
        every { readerProvider.read(any()) } returns "[]"

        // act
        val result = sut.getByKey("unknown")

        // assert
        assertNull(result)
    }
}
