package br.dev.singular.overview.data.model

import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.model.provider.StreamingsData

data class HomeData(
    val streams: StreamingsData = StreamingsData(),
    val recommendedMedias: List<MediaEntity> = listOf()
)
