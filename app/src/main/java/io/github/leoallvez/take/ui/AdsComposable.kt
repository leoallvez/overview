package io.github.leoallvez.take.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdsBannerBottomAppBar(@StringRes bannerId: Int, isVisible: Boolean) {
    if(isVisible) {
        BottomAppBar(
            backgroundColor = Color.Black,
            modifier = Modifier.height(50.dp).padding(0.dp),
            content = {
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
        )
    }
}
