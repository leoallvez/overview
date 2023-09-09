package br.com.deepbyte.overview.data.model

import br.com.deepbyte.overview.data.model.media.MediaEntity
import br.com.deepbyte.overview.data.model.provider.StreamingsData

data class HomeData(
    val streamingsData: StreamingsData = StreamingsData(),
    val recommendedMedias: List<MediaEntity> = listOf()
)
