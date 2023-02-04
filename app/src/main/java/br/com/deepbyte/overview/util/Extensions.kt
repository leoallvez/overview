package br.com.deepbyte.overview.util

import android.content.Context
import androidx.navigation.NavBackStackEntry
import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.DiscoverParams
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.provider.StreamingService
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.ui.ScreenNav
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

fun StreamingService.createDiscoverParams(
    media: Media
) = DiscoverParams(
    apiId = apiId,
    screenTitle = name,
    mediaId = media.apiId,
    mediaType = media.getType()
)

fun Genre.createDiscoverParams(
    media: Media
) = DiscoverParams(
    apiId = apiId,
    screenTitle = name,
    mediaId = media.apiId,
    mediaType = media.getType()
)

fun <T : Media> DataResult<ListResponse<T>>.toList(): List<T> {
    val isValid = this is DataResult.Success
    val medias = data?.results ?: listOf()
    return (if (isValid) medias.filter { it.adult.not() } else listOf())
}

const val DESERIALIZATION_ERROR_MSG = "deserialization exception"
