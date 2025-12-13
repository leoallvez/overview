package br.dev.singular.overview.presentation.ui.utils

import android.app.Activity
import android.content.pm.ActivityInfo

fun Activity.setFullscreen(isFullscreen: Boolean) {
    requestedOrientation = if (isFullscreen) {
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    } else {
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
