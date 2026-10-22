package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class UserUiModel(
    val id: String,
    val name: String,
    val email: String,
    val photoURL: String,
    @get:DrawableRes
    val previewDrawableRes: Int? = null,
)
