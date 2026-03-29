package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PersonUiModel(
    val id: Long,
    val job: String,
    val age: String,
    val name: String,
    val birthday: String,
    val deathDay: String,
    val biography: String,
    val character: String,
    val profileURL: String,
    val placeOfBirth: String,
    @get:DrawableRes
    val previewDrawableRes: Int?,
    val tvShows: ImmutableList<MediaUiModel>,
    val movies: ImmutableList<MediaUiModel>
)
