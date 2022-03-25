package io.github.leoallvez.take.helper

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import timber.log.Timber

inline fun <reified T : Any> String.fromJsonOrNull(): T? =
    try {
        Gson().fromJson(this, T::class.java)
    } catch (e: JsonSyntaxException) {
        Timber.e("Error to deserialize json: $e")
        null
    }