package io.github.leoallvez.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.github.leoallvez.firebase.RemoteConfigKey.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class RemoteConfigWrapperTest {

    private lateinit var remoteSource: RemoteSource
    private lateinit var remote: FirebaseRemoteConfig

    @Before
    fun setup() {
        remote = mockk()
        remoteSource = RemoteConfigWrapper(remote)
    }

    @Test
    fun example() {
        every { remoteSource.getString(UNIT_TEST_KEY) } returns "example"

        assertEquals("example", "example")
    }

}