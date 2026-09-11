package br.dev.singular.overview.data.remote.config

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class RemoteConfigKeyTest {

    @Test
    fun `getKeyByRegion should return BR key for BR region`() {
        val result = RemoteConfigKey.getKeyByRegion("BR")
        result shouldBeEqualTo RemoteConfigKey.STREAM_BR
    }

    @Test
    fun `getKeyByRegion should return US key for US region`() {
        val result = RemoteConfigKey.getKeyByRegion("US")
        result shouldBeEqualTo RemoteConfigKey.STREAM_US
    }

    @Test
    fun `getKeyByRegion should return ES key for ES region`() {
        val result = RemoteConfigKey.getKeyByRegion("ES")
        result shouldBeEqualTo RemoteConfigKey.STREAM_ES
    }

    @Test
    fun `getKeyByRegion should return US key for unknown region`() {
        val result = RemoteConfigKey.getKeyByRegion("UK")
        result shouldBeEqualTo RemoteConfigKey.STREAM_US
    }
}
