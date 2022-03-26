package io.github.leoallvez.firebase

interface RemoteSource {
    fun getString(key: RemoteConfigKey): String
    fun getBoolean(key: RemoteConfigKey): Boolean
}