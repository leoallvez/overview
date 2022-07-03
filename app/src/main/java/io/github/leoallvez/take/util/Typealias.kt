package io.github.leoallvez.take.util

import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.ApiResult

typealias MediaListResult = ApiResult<ListContentResponse<MediaItem>>
typealias MediaDetailResult = ApiResult<MediaDetailResponse>