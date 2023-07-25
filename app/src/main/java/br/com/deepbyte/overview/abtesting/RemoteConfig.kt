package br.com.deepbyte.overview.abtesting

interface RemoteConfig<T> {
    fun execute(): T
}
