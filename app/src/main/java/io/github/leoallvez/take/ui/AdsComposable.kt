package io.github.leoallvez.take.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import io.github.leoallvez.take.R

@Composable
fun AdsBanner(@StringRes bannerId: Int, isVisible: Boolean, modifier: Modifier = Modifier) {
    if (isVisible) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.default_padding))
                .height(dimensionResource(R.dimen.ads_banner_height)),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = context.getString(bannerId)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
