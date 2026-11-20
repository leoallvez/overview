package br.dev.singular.overview.navigation

import androidx.compose.runtime.Immutable

@Immutable
data class FeatureFlags(
    val showAds: Boolean,
    val showAiPrediction: Boolean
)
