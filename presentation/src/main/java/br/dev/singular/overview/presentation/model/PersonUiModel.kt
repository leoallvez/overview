package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
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
    val previewContent: Painter = ColorPainter(Color.Gray),
    val tvShows: ImmutableList<MediaUiModel>,
    val movies: ImmutableList<MediaUiModel>
)
