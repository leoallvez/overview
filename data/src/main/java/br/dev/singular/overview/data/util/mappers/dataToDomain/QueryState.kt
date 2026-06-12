package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.domain.model.QueryState

fun QueryDataState.toDomain() = QueryState(
    key = path,
    type = type.toDomain(),
    isLiked = isLiked,
    catalog = catalog?.toDomain(),
    genre = genre?.toDomain(),
    query = query,
    page = page
)
