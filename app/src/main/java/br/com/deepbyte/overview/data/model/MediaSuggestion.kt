package br.com.deepbyte.overview.data.model

import androidx.compose.runtime.Immutable

@Immutable
class MediaSuggestion(
    val order: Int,
    val type: String,
    val titleResourceId: String,
    val items: List<MediaItem>
)