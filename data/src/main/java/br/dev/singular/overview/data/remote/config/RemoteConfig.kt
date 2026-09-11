package br.dev.singular.overview.data.remote.config

interface RemoteConfig<T> {
    fun execute(): T
}
