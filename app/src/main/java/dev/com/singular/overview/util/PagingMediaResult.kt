package dev.com.singular.overview.util

import dev.com.singular.overview.data.api.response.PagingResponse
import dev.com.singular.overview.data.model.media.Media
import dev.com.singular.overview.data.source.DataResult

typealias PagingMediaResult = DataResult<PagingResponse<Media>>
