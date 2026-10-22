package br.dev.singular.overview.data.network

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.util.Locale

class LocaleProviderTest {

    @Test
    fun `language should return current locale language tag`() {
        Locale.setDefault(Locale.US)
        val provider = LocaleProvider()
        provider.language shouldBeEqualTo "en-US"
    }

    @Test
    fun `region should return current locale country`() {
        Locale.setDefault(Locale.US)
        val provider = LocaleProvider()
        provider.region shouldBeEqualTo "US"
    }
}
