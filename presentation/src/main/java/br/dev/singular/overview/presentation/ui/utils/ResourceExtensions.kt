package br.dev.singular.overview.presentation.ui.utils

import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

/**
 * Returns the float value of a dimension resource.
 *
 * @param id The resource ID of the dimension.
 * @return The float value.
 */
@Composable
@ReadOnlyComposable
fun floatResource(@DimenRes id: Int): Float {
    val context = LocalContext.current
    val outValue = TypedValue()
    context.resources.getValue(id, outValue, true)
    return outValue.float
}
