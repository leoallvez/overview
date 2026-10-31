package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class EpisodeUiModel(
    val title: String,
    val duration: String,
    // https://image.tmdb.org/t/p/w154/vlFDprUHK2yNBDsbIXsFNP8ludv.jpg
    val imageURL: String,
    val episodeNumber: Int,
    @get:DrawableRes
    val previewDrawableRes: Int?,
)
