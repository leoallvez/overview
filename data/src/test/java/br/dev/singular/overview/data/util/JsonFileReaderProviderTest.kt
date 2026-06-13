package br.dev.singular.overview.data.util

import android.content.Context
import android.content.res.AssetManager
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException

class JsonFileReaderProviderTest {

    private val context: Context = mockk()
    private val assets: AssetManager = mockk()
    private val provider = JsonFileReaderProvider(context)

    @Test
    fun `read should return file content when file exists`() {
        // arrange
        val filePath = "test.json"
        val content = "{\"key\": \"value\"}"
        val inputStream = ByteArrayInputStream(content.toByteArray())

        every { context.assets } returns assets
        every { assets.open(filePath) } returns inputStream

        // act
        val result = provider.read(filePath)

        // assert
        result shouldBeEqualTo content
    }

    @Test
    fun `read should return empty string when IOException occurs`() {
        // arrange
        val filePath = "invalid.json"
        every { context.assets } returns assets
        every { assets.open(filePath) } throws IOException()

        // act
        val result = provider.read(filePath)

        // assert
        result shouldBeEqualTo ""
    }
}
