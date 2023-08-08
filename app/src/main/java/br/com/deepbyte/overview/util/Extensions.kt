package br.com.deepbyte.overview.util

import android.content.Context
import android.content.res.Resources.NotFoundException
import androidx.navigation.NavBackStackEntry
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.UiState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.IOException
import timber.log.Timber
import java.lang.reflect.Type

typealias MediaItemClick = (apiId: Long, mediaType: String?) -> Unit

inline fun <reified T> String.fromJson(): T? = try {
    val moshi = Moshi.Builder().build()
    moshi.adapter(T::class.java).fromJson(this)
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

// TODO: Finish unit tests for another functions
fun Context.getStringByName(resource: String): String? {
    return try {
        val resourceId = this.resources
            .getIdentifier(resource, "string", this.packageName)
        return this.getString(resourceId)
    } catch (e: NotFoundException) {
        null
    }
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

fun NavBackStackEntry.getBackToHome(): Boolean {
    return arguments?.getBoolean(ScreenNav.BACK_TO_HOME_PARAM) ?: false
}

fun NavBackStackEntry.getApiId(): Long = arguments?.getLong(ScreenNav.ID_PARAM) ?: 0

fun NavBackStackEntry.getStreamingParams(): Streaming {
    val json = arguments?.getString(ScreenNav.JSON_PARAM) ?: ""
    return json.fromJson() ?: Streaming()
}

fun List<Long>.joinToStringWithPipe() = joinToString(separator = "|") { it.toString() }
fun List<Long>.joinToStringWithComma() = joinToString(separator = ",") { it.toString() }

fun <T> T.toUiState(isValid: (T) -> Boolean = { true }) =
    if (isValid(this)) { UiState.Success(data = this) } else { UiState.Error() }

const val DESERIALIZATION_ERROR_MSG = "deserialization exception"
