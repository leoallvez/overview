package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.model.provider.Streaming
import io.mockk.MockKAnnotations
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.Before
import org.junit.Test

class StreamingsRemoteConfigTest : LocalAndRemoteConfigTest() {

    private lateinit var _streaming: RemoteConfig<List<Streaming>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _streaming = StreamingRemoteConfig(jsonFileReader, remoteSource)
    }

    @Test
    fun `should be a non-empty list when local is not empty and remote is empty`() {
        // Arrange
        everyLocalAndRemote(local = JSON, remote = EMPTY)
        // Act
        val list = _streaming.execute()
        // Assert
        list.shouldNotBeEmpty()
    }

    @Test
    fun `should be a non-empty list when local is empty and remote is not empty`() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = JSON)
        // Act
        val list = _streaming.execute()
        // Assert
        list.shouldNotBeEmpty()
    }

    @Test
    fun `should be an empty list when local is empty and remote is empty`() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = EMPTY)
        // Act
        val list = _streaming.execute()
        // Assert
        list.shouldBeEmpty()
    }

    @Test
    fun `should be an empty list when local is an invalid json and remote is invalid json`() {
        // Arrange
        everyLocalAndRemote(local = INVALID_JSON, remote = INVALID_JSON)
        // Act
        val list = _streaming.execute()
        // Assert
        list.shouldBeEmpty()
    }

    companion object {
        private const val JSON = """
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
