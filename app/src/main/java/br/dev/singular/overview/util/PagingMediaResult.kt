package br.dev.singular.overview.util

import br.dev.singular.overview.data.api.response.PagingResponse
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.source.DataResult

typealias PagingMediaResult = DataResult<PagingResponse<Media>>
