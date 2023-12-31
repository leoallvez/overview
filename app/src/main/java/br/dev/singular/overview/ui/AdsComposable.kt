package br.dev.singular.overview.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.dev.singular.overview.BuildConfig
import br.dev.singular.overview.R
import br.dev.singular.overview.ui.theme.PrimaryBackground
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdsBanner(
    @StringRes prodBannerId: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .background(color = PrimaryBackground)
                .padding(vertical = dimensionResource(R.dimen.default_padding))
                .height(dimensionResource(R.dimen.ads_banner_height)),
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

@Composable
fun AdsMediumRectangle(
    @StringRes prodBannerId: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
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
