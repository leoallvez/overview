package br.com.deepbyte.take.util

import android.content.Context
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import br.com.deepbyte.take.data.model.DiscoverParams
import br.com.deepbyte.take.data.model.MediaItem
import br.com.deepbyte.take.data.model.MediaSuggestion
import br.com.deepbyte.take.data.model.Suggestion
import br.com.deepbyte.take.ui.ScreenNav
import okio.IOException
import timber.log.Timber

typealias MediaItemClick = (apiId: Long, mediaType: String?) -> Unit

inline fun <reified T> String.fromJson(): T? = try {
    val moshi = Moshi.Builder().build()
    moshi.adapter(T::class.java).fromJson(this)
} catch (io: IOException) {
    Timber.e(message = "$DESERIALIZATION_ERROR_MSG: ${io.stackTrace}")
    null
}

inline fun <reified T> String.parseToList(): List<T> = try {
    val type = TypeToken.getParameterized(List::class.java, T::class.java).type
    if (this.isNotEmpty()) Gson().fromJson(this, type) else listOf()
} catch (e: JsonSyntaxException) {
    Timber.e(message = "$DESERIALIZATION_ERROR_MSG: ${e.stackTrace}")
    listOf()
}

// TODO: Finish unit tests for another functions
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
    val id = arguments?.getLong(ScreenNav.ID_PARAM)
    val type = arguments?.getString(ScreenNav.TYPE_PARAM)
    return Pair(id ?: 0, type ?: "")
}

fun NavBackStackEntry.getApiId(): Long = arguments?.getLong(ScreenNav.ID_PARAM) ?: 0

fun NavBackStackEntry.getDiscoverParams(): DiscoverParams {
    val json = arguments?.getString(ScreenNav.JSON_PARAM) ?: ""
    return json.fromJson() ?: DiscoverParams()
}

const val DESERIALIZATION_ERROR_MSG = "deserialization exception"
