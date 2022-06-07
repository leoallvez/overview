package io.github.leoallvez.take.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdsBanner(@StringRes bannerId: Int, isVisible: Boolean, modifier: Modifier) {
    if(isVisible) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                AdView(context).apply {
                    adSize = AdSize.BANNER
                    adUnitId = context.getString(bannerId)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
