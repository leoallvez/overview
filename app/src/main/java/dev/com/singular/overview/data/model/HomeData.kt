package dev.com.singular.overview.data.model

import dev.com.singular.overview.data.model.media.MediaEntity
import dev.com.singular.overview.data.model.provider.StreamingsData

data class HomeData(
    val streams: StreamingsData = StreamingsData(),
    val recommendedMedias: List<MediaEntity> = listOf()
)
