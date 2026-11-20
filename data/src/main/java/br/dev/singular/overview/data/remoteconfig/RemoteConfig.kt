package br.dev.singular.overview.data.remoteconfig

interface RemoteConfig<T> {
    fun execute(): T
}
