package br.dev.singular.overview.data.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class MediaDataTypeTest {

    @Test
    fun `fromKey should return MOVIE when key is movie`() {
        MediaDataType.fromKey("movie") shouldBeEqualTo MediaDataType.MOVIE
    }

    @Test
    fun `fromKey should return TV when key is tv`() {
        MediaDataType.fromKey("tv") shouldBeEqualTo MediaDataType.TV
    }

    @Test
    fun `fromKey should return UNKNOWN when key is invalid`() {
        MediaDataType.fromKey("invalid") shouldBeEqualTo MediaDataType.UNKNOWN
    }
}
