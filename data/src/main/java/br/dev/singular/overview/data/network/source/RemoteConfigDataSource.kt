package br.dev.singular.overview.data.network.source

interface RemoteConfigDataSource<T> {
    fun getAll(): T
}
