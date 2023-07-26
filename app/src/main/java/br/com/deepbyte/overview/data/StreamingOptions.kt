package br.com.deepbyte.overview.data

import br.com.deepbyte.overview.data.model.provider.Streaming
import com.squareup.moshi.Json

data class StreamingOptions(
    @field:Json(name = "streamings_by_regions")
    val streamingsByRegion: Map<String, List<Streaming>> = mapOf()
)
