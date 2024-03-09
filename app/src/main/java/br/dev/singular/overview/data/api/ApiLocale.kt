package br.dev.singular.overview.data.api

import java.util.Locale
import javax.inject.Inject

interface IApiLocale {
    val region: String
    val language: String
}

class ApiLocale @Inject constructor() : IApiLocale {

    private val locale: Locale = Locale.getDefault()

    override val region: String = locale.country

    override val language: String = locale.toLanguageTag()
}
