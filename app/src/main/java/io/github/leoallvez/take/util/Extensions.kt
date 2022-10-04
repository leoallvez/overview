package io.github.leoallvez.take.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.MediaSuggestion
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.ui.Screen
import okio.IOException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

inline fun <reified T> String.fromJson(): T? = try {
    val moshi = Moshi.Builder().build()
    moshi.adapter(T::class.java).fromJson(this)
} catch (io: IOException) {
    Timber.e(message = "$DESERIALIZATION_ERROR_MSG: ${io.stackTrace}")
    null
}

inline fun <reified T> String.parseToList(): List<T> = try {
    val type = TypeToken.getParameterized(List::class.java, T::class.java).type
    if(this.isNotEmpty()) Gson().fromJson(this, type) else listOf()
} catch (e: JsonSyntaxException) {
    Timber.e(message = "$DESERIALIZATION_ERROR_MSG: ${e.stackTrace}")
    listOf()
}

//TODO: Finish unit tests for another functions
fun Context.getStringByName(resource: String): String {
    val resourceId = this.resources
        .getIdentifier(resource, "string", this.packageName)
    return this.getString(resourceId)
}

fun Map.Entry<Suggestion, List<MediaItem>>.toMediaSuggestion(): MediaSuggestion {
    val suggestion = this.key
    val items = this.value
    return MediaSuggestion(
        order = suggestion.order,
        type = suggestion.type,
        titleResourceId = suggestion.titleResourceId,
        items = items
    )
}

fun Array<out MediaItem>.removeRepeated(itemsToRemove: List<MediaItem>): List<MediaItem> {
    return this.filterNot { a -> itemsToRemove.any { b -> b equivalent a } }
}

private infix fun MediaItem.equivalent(other: MediaItem): Boolean {
    return this.apiId == other.apiId && this.suggestionId == other.suggestionId
}

fun NavBackStackEntry.getParams(): Pair<Long, String> {
    val id = arguments?.getLong(Screen.ID_PARAM)
    val type = arguments?.getString(Screen.TYPE_PARAM)
    return Pair(id ?: 0, type ?: "")
}

const val DESERIALIZATION_ERROR_MSG = "deserialization exception"

