package br.dev.singular.overview.presentation.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber

/**
 * Returns the float value of a dimension resource.
 *
 * @param id The resource ID of the dimension.
 * @return The float value.
 */
@Composable
@ReadOnlyComposable
internal fun floatResource(@DimenRes id: Int): Float {
    val context = LocalContext.current
    val outValue = TypedValue()
    context.resources.getValue(id, outValue, true)
    return outValue.float
}

@SuppressLint("DiscouragedApi")
internal fun Context.getStringByName(resource: String): String? = try {
    val resourceId = resources.getIdentifier(resource, "string", packageName)
    getString(resourceId)
} catch (e: NotFoundException) {
    Timber.e(e)
    null
}
