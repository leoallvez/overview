package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.presentation.BuildConfig

fun buildImageFullURL(path: String) = "${BuildConfig.IMG_URL}$path"

fun buildPosterURL(path: String) = "${BuildConfig.POSTER_URL}$path"

fun buildThumbnailUrl(key: String) =
    "${BuildConfig.THUMBNAIL_BASE_URL}/$key/${BuildConfig.THUMBNAIL_QUALITY}"

fun formatRuntime(runtime: Int): String {
    if (runtime <= 0) return ""
    val hours = runtime / 60
    val minutes = runtime % 60
    return buildString {
        if (hours > 0) append("${hours}h ")
        if (minutes > 0 || hours == 0) append("${minutes}min")
    }.trim()
}
