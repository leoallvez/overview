package br.dev.singular.overview.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.dev.singular.overview.presentation.BuildConfig
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.utils.UiPlaceholder
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
    isVisible: Boolean = true
) {
    if (!isVisible) return

    BaseAdView(
        adSize = AdSize.BANNER,
        prodBannerId = prodBannerId,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Background)
            .height(dimensionResource(R.dimen.spacing_15x))
    )
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
    isVisible: Boolean = true
) {
    if (!isVisible) return

    BaseAdView(
        adSize = AdSize.MEDIUM_RECTANGLE,
        prodBannerId = prodBannerId,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Background)
            .height(250.dp)
    )
}

/**
 * A base composable for displaying ads with preview support.
 */
@Composable
private fun BaseAdView(
    adSize: AdSize,
    @StringRes prodBannerId: Int,
    modifier: Modifier = Modifier,
    previewText: String = "Ad Area"
) {
    if (BuildConfig.DEBUG) {
        UiPlaceholder(modifier = modifier, text = previewText)
    } else {
        val adUnitId = BuildConfig.DEBUG_BANNER_ID.ifEmpty { stringResource(prodBannerId) }
        AndroidView(
            modifier = modifier,
            factory = { context ->
                AdView(context).apply {
                    setAdSize(adSize)
                    this.adUnitId = adUnitId
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Preview
@Composable
private fun UiAdsPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Background)
            .padding(dimensionResource(R.dimen.spacing_3x)),
        verticalArrangement = spacedBy(
            dimensionResource(R.dimen.spacing_3x)
        )
    ) {
        UiAdsBanner(prodBannerId = R.string.debug_banner)
        UiAdsMediumRectangle(prodBannerId = R.string.debug_banner)
    }
}
