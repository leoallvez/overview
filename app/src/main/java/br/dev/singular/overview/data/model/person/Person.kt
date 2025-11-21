package br.dev.singular.overview.data.model.person

import br.dev.singular.overview.presentation.BuildConfig
import com.squareup.moshi.Json

data class Person(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = "",
    val order: Int = 0,
    val job: String = "",
    private val character: String = "",
    @field:Json(name = "profile_path")
    private val profilePath: String = ""
) {
    fun getProfileURL() = "${BuildConfig.IMG_URL}/$profilePath"
    fun getFormattedCharacterName(): String {
        return character.substringBefore('/')
    }
}
