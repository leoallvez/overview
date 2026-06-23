package br.dev.singular.overview.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import timber.log.Timber

@SuppressLint("DiscouragedApi")
fun Context.getStringByName(resource: String): String? = try {
    val resourceId = resources.getIdentifier(resource, "string", packageName)
    getString(resourceId)
} catch (e: NotFoundException) {
    Timber.e(e)
    null
}
