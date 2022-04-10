package io.github.leoallvez.take.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type

inline fun <reified T> String.fromJson(): T? = try {
    val moshi = Moshi.Builder().build()
    moshi.adapter(T::class.java).fromJson(this)
} catch (e: Exception) {
    Timber.e(message = "deserialization exception: ${e.stackTrace}")
    null
}

fun <T> String.getList(clazz: Class<T>?): List<T>? {

    return try {
        val typeOfT: Type = TypeToken.getParameterized(MutableList::class.java, clazz).type
        Gson().fromJson(this, typeOfT)
    } catch (io: IOException) {
        null
    }
}

