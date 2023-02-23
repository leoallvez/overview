package br.com.deepbyte.overview.data.model.media

data class Similar<T : Media>(val results: List<T> = listOf())
