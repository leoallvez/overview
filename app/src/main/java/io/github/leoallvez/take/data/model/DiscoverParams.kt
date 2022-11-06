package io.github.leoallvez.take.data.model

data class DiscoverParams(
    val providerId: Long,
    val providerName: String,
    val mediaId: Long,
    val mediaType: String
) {
    fun toJson(): String {
        return """{
            "providerId":$providerId,
            "providerName":"$providerName",
            "mediaId":$mediaId,
            "mediaType": "$mediaType"
        }""".trimMargin()
    }
}
