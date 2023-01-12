package br.com.deepbyte.overview.data.model.person

import br.com.deepbyte.overview.BuildConfig
import com.squareup.moshi.Json

data class Person(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = "",
    @field:Json(name = "profile_path")
    val profilePath: String = "",
) {
    fun getProfileImage() = "${BuildConfig.IMG_URL}/$profilePath"
}
