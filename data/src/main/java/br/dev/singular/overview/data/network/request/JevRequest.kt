package br.dev.singular.overview.data.network.request

import br.dev.singular.overview.data.model.MediaDataType
import kotlinx.serialization.Serializable

@Serializable
data class JevRequest(
    val model: String,
    val state: JevState,
    val questions: Map<String, JevQuestion>
)

@Serializable
data class JevState(
    val media: JevMedia,
    val favorites: JevFavorites
)

@Serializable
data class JevMedia(
    val originalTitle: String,
    val type: MediaDataType,
    val genres: List<String>
)

@Serializable
data class JevFavorites(
    val titles: List<JevTitle>,
    val genres: Map<String, Int>
)

@Serializable
data class JevTitle(
    val originalTitle: String,
    val type: MediaDataType
)

@Serializable
data class JevQuestion(
    val type: String,
    val instructions: String,
    val criteria: List<String>
)
