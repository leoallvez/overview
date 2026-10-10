package br.dev.singular.overview.data.model

data class PredictionInputDataModel(
    val id: Long,
    val type: MediaDataType,
    val title: String,
    val genres: List<String>
)
