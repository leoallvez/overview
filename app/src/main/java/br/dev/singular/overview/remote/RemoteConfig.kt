package br.dev.singular.overview.remote

interface RemoteConfig<T> {
    fun execute(): T
}
