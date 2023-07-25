package br.com.deepbyte.overview.data.model.provider

data class StreamingConfig(
    private val active: Boolean = false,
    private val streamingsByRegion: Map<String, List<Streaming>> = mapOf()
)
