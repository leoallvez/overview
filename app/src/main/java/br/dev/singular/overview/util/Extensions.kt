package br.dev.singular.overview.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import androidx.navigation.NavBackStackEntry
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.UiState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.IOException
import timber.log.Timber
import java.lang.reflect.Type

typealias MediaItemClick = (apiId: Long, mediaType: String?) -> Unit

inline fun <reified T> String?.fromJson(): T? = try {
    if (this != null) {
        val moshi = Moshi.Builder().build()
        moshi.adapter(T::class.java).fromJson(this)
    } else {
        null
    }
} catch (io: IOException) {
    Timber.e(message = "$DESERIALIZATION_ERROR_MSG: ${io.stackTrace}")
    null
}

inline fun <reified T> T.toJson(): String {
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter<Any>(T::class.java)
    return jsonAdapter.toJson(this)
}

inline fun <reified T> String.parseToList(): List<T> = try {
    val type: Type = Types.newParameterizedType(List::class.java, T::class.java)
    val moshi = Moshi.Builder().build()
    val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
    adapter.fromJson(this) ?: listOf()
} catch (e: Exception) {
    Timber.e(message = "$DESERIALIZATION_ERROR_MSG: ${e.stackTrace}")
    listOf()
}

@SuppressLint("DiscouragedApi")
fun Context.getStringByName(resource: String): String? = try {
    val resourceId = resources.getIdentifier(resource, "string", packageName)
    getString(resourceId)
} catch (e: NotFoundException) {
    null
}

fun NavBackStackEntry.getParams(): Pair<Long, String> {
    val id = arguments?.getLong(ScreenNav.ID_PARAM)
    val type = arguments?.getString(ScreenNav.TYPE_PARAM)
    return Pair(id ?: 0, type ?: "")
}

fun NavBackStackEntry.getApiId(): Long = arguments?.getLong(ScreenNav.ID_PARAM) ?: 0

fun List<Long>.joinToStringWithPipe() = joinToString(separator = "|") { it.toString() }

fun <T> T.toUiState(isValid: (T) -> Boolean = { true }) =
    if (isValid(this)) {
        UiState.Success(data = this)
    } else {
        UiState.Error()
    }

fun <T> DataResult<out T>.toUiState(): UiState<T?> {
    val isSuccess = this is DataResult.Success
    return if (isSuccess) UiState.Success(this.data) else UiState.Error()
}

fun Long?.isNull() = this == null

fun Long?.toStringOrEmpty() = if (isNull()) String() else toString()

const val DESERIALIZATION_ERROR_MSG = "deserialization exception"
