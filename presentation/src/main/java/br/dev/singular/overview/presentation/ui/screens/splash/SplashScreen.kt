package br.dev.singular.overview.presentation.ui.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagSplash
import br.dev.singular.overview.presentation.ui.theme.Background
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onToHome: () -> Unit) {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(key1 = Unit) {
        TagManager.logScreenView(TagSplash.PATH)
        scale.animateTo(
            targetValue = 0.6f,
            animationSpec = tween(
                durationMillis = 900,
                easing = {
                    OvershootInterpolator(5f).getInterpolation(it)
                }
            )
        )
        delay(2000L)
        onToHome()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Background),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.scale(scale.value).size(400.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.launcher_playstore),
                contentDescription = null,
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
