package br.dev.singular.overview.data.network

import java.util.Locale
import javax.inject.Inject

interface ILocaleProvider {
    val region: String
    val language: String
}

// Named as ApiLocale on app module
class LocaleProvider @Inject constructor() : ILocaleProvider {

    private val locale: Locale by lazy { Locale.getDefault() }

    override val region: String by lazy { locale.country }

    override val language: String by lazy { locale.toLanguageTag() }
}
