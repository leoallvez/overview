package br.dev.singular.overview.data.model.media

data class Similar<T : Media>(val results: List<T> = listOf())
