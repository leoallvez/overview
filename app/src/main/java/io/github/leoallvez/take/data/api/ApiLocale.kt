package io.github.leoallvez.take.data.api

import java.util.*
import javax.inject.Inject

class ApiLocale @Inject constructor(): IApiLocale {
    override val locale: Locale = Locale.getDefault()
    override val region: String = locale.country

    override fun language(): String = when (locale.language) {
        "pt" -> "pt-BR"
        else -> "en"
    }
}

interface IApiLocale {
    val locale: Locale
    val region: String
    fun language(): String
}