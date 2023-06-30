package br.com.deepbyte.overview.util

import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult

typealias PagingMediaResult = DataResult<PagingResponse<Media>>
