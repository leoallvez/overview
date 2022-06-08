package io.github.leoallvez.take.data.model

import androidx.compose.runtime.Immutable

@Immutable
class MediaSuggestion(
    val order: Int,
    val titleResourceId: String,
    val items: List<MediaItem>
)