package io.github.leoallvez.take.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdsBanner(@StringRes bannerId: Int, isDisplayed: Boolean) {
    if(isDisplayed) { //TODO: <= find a better solution that "if"
        Box(modifier = Modifier.padding(10.dp)) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
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
}