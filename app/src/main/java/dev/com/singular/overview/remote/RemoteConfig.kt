package dev.com.singular.overview.remote

interface RemoteConfig<T> {
    fun execute(): T
}
