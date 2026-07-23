package br.dev.singular.overview.data.util.mappers

import br.dev.singular.overview.data.model.QueryDataState

fun QueryDataState.toParams(extraParams: Map<String, String>): Map<String, String> {
    return buildMap {
        putAll(extraParams)
        genre?.apply { put("with_genres", id.toString()) }
        catalog?.apply { put("with_watch_providers", id.toString()) }
    }
}
