package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.BuildConfig
import com.squareup.moshi.Json

data class SearchPersonResponse(
    private val page: Int = 0,
    val results: List<SearchPersonResult> = listOf()
)

data class SearchPersonResult(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val name: String = "",
    @field:Json(name = "profile_path")
    override val profilePath: String = "",
) : Person

interface Person {
    val profilePath: String
    fun getProfileImage() = "${BuildConfig.IMG_URL}/$profilePath"
}

