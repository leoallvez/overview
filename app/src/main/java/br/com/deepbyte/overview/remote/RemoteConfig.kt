package br.com.deepbyte.overview.remote

interface RemoteConfig<T> {
    fun execute(): T
}
