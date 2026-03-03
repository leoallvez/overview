package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.presentation.BuildConfig

fun buildImageFullURL(path: String) = "${BuildConfig.IMG_URL}$path}"

fun buildPosterURL(path: String) = "${BuildConfig.POSTER_URL}$path}"
