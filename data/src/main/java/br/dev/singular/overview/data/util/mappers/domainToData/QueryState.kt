package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.domain.model.QueryState

internal fun QueryState.toData(
    path: String,
) = QueryDataState(
    path = path,
    type = type.toData(),
    isLiked = isLiked,
    query = query,
    catalog = catalog?.toData(),
    genre = genre?.toData(),
    page = page
)

fun QueryState.toData() = QueryDataState(
    path = key,
    type = type.toData(),
    isLiked = isLiked,
    catalog = catalog?.toData(),
    genre = genre?.toData(),
    query = query,
    page = page
)
