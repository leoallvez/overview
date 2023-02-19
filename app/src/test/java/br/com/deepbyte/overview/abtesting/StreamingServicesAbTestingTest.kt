package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.model.provider.Streaming
import io.mockk.MockKAnnotations
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StreamingServicesAbTestingTest : LocalAndRemoteTest() {

    private lateinit var _streaming: AbTesting<List<Streaming>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _streaming = StreamingServicesAbTesting(jsonFileReader, remoteSource)
    }

    @Test
    fun execute_localIsNotEmptyAndRemoteIsEmpty_listIsNotEmpty() {
        // Arrange
        everyLocalAndRemote(local = VALID_JSON, remote = EMPTY)
        // Act
        val list = _streaming.execute()
        // Assert
        Assert.assertTrue(list.isNotEmpty())
    }

    @Test
    fun execute_localIsEmptyAndRemoteNotIsEmpty_listIsNotEmpty() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = VALID_JSON)
        // Act
        val list = _streaming.execute()
        // Assert
        Assert.assertTrue(list.isNotEmpty())
    }

    @Test
    fun execute_localIsEmptyAndRemoteIsEmpty_listIsEmpty() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = EMPTY)
        // Act
        val list = _streaming.execute()
        // Assert
        Assert.assertTrue(list.isEmpty())
    }

    @Test
    fun execute_localIsInvalidAndRemoteIsValid_listIsEmpty() {
        // Arrange
        everyLocalAndRemote(local = INVALID_JSON, remote = INVALID_JSON)
        // Act
        val list = _streaming.execute()
        // Assert
        Assert.assertTrue(list.isEmpty())
    }

    companion object {
        const val VALID_JSON = """
            [
                {
                    "display_priority": 4,
                    "provider_name": "Amazon Prime",
                    "provider_id": 9
                }
            ]
        """
    }
}
