package io.github.leoallvez.take.util

import com.squareup.moshi.Moshi
import timber.log.Timber
import java.lang.Exception

inline fun <reified T> String.fromJson(): T? = try {
    val moshi = Moshi.Builder().build()
    moshi.adapter(T::class.java).fromJson(this)
} catch (e: Exception) {
    Timber.e(message = "deserialization exception: ${e.stackTrace}")
    null
}

