package br.com.deepbyte.overview.data.model.provider

import com.squareup.moshi.Json

data class Provider(
    private val link: String,
    @field:Json(name = "flatrate")
    private val flatRate: List<ProviderPlace>?
) {
    fun getOrderedFlatRate(): List<ProviderPlace> {
        val validFlatRate = flatRate?.filter { isValid(it.apiId) } ?: listOf()
        return validFlatRate.sortedBy { it.displayPriority }
    }

    private fun isValid(apiId: Long) = (apiId == NETFLIX_WITH_ADS_ID).not()

    companion object {
        private const val NETFLIX_WITH_ADS_ID = 1796L
    }
}
