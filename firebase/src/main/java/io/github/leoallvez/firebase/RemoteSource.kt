package io.github.leoallvez.firebase

interface RemoteSource {
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
}