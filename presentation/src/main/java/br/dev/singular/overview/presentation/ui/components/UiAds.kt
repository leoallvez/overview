package br.dev.singular.overview.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.dev.singular.overview.presentation.BuildConfig
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.Background
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * A composable that displays a banner ad.
 *
 * @param prodBannerId The string resource for the production banner ID.
 * @param modifier The modifier to be applied to the ad.
 * @param isVisible Whether the ad should be visible.
 */
@Composable
fun UiAdsBanner(
    @StringRes prodBannerId: Int,
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    if (isVisible) {
        Column {
            AndroidView(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = Background)
                    .padding(vertical = dimensionResource(R.dimen.spacing_1x))
                    .height(dimensionResource(R.dimen.spacing_15x)),
                factory = { context ->
                    val debugBannerId = BuildConfig.DEBUG_BANNER_ID
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = debugBannerId.ifEmpty { context.getString(prodBannerId) }
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}

/**
 * A composable that displays a medium rectangle ad.
 *
 * @param prodBannerId The string resource for the production banner ID.
 * @param modifier The modifier to be applied to the ad.
 * @param isVisible Whether the ad should be visible.
 */
@Composable
fun UiAdsMediumRectangle(
    @StringRes prodBannerId: Int,
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    if (isVisible) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_5x))
                .height(250.dp),
            factory = { context ->
                val debugBannerId = BuildConfig.DEBUG_BANNER_ID
                AdView(context).apply {
                    setAdSize(AdSize.MEDIUM_RECTANGLE)
                    adUnitId = debugBannerId.ifEmpty { context.getString(prodBannerId) }
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
