package br.com.deepbyte.overview.data.model.provider

data class StreamingConfig(
    val active: Boolean = false,
    val streamingsByRegion: Map<String, List<Streaming>> = mapOf()
)
