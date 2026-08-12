package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.presentation.BuildConfig

internal fun buildImageFullURL(path: String) = "${BuildConfig.IMG_URL}$path"

internal fun buildPosterURL(path: String) = "${BuildConfig.POSTER_URL}$path"

internal fun extractPath(url: String): String {
    if (url.isBlank()) return ""
    return url.substringAfter(BuildConfig.POSTER_URL, url)
}

internal fun buildThumbnailUrl(key: String) =
    "${BuildConfig.THUMBNAIL_BASE_URL}/$key/${BuildConfig.THUMBNAIL_QUALITY}"

internal fun formatRuntime(runtime: Int): String {
    if (runtime <= 0) return ""
    val hours = runtime / 60
    val minutes = runtime % 60
    return buildString {
        if (hours > 0) append("${hours}h ")
        if (minutes > 0 || hours == 0) append("${minutes}min")
    }.trim()
}
