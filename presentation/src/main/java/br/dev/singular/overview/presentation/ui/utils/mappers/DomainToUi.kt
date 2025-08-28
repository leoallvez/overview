package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.BuildConfig
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType

fun List<Suggestion>.toUi() = associate { it.key to it.medias.map(Media::toUi) }

fun Media.toUi() = MediaUiModel(
    id = id,
    type = type.toUi(),
    title = title,
    isLiked = isLiked,
    posterURL = buildImageFullURL(posterPath)
)

internal fun MediaType.toUi() = when(this) {
    MediaType.MOVIE -> MediaUiType.MOVIE
    MediaType.TV -> MediaUiType.TV
    else -> MediaUiType.ALL
}

fun <T> UseCaseState<T>.toUi() = when (this) {
    is UseCaseState.Failure -> UiState.Error()
    is UseCaseState.Success -> UiState.Success(data)
}

fun <T, R> UseCaseState<T>.toUi(transform: (T) -> R): UiState<R> {
    return when (this) {
        is UseCaseState.Success -> UiState.Success(transform(data))
        is UseCaseState.Failure -> UiState.Error()
    }
}

fun buildImageFullURL(path: String) = "${BuildConfig.IMG_URL}$path}"
