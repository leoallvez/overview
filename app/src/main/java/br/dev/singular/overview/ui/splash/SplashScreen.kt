package br.dev.singular.overview.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagSplash
import br.dev.singular.overview.ui.DefaultViewModel
import br.dev.singular.overview.ui.navigation.wrappers.SplashNavigate
import br.dev.singular.overview.ui.theme.PrimaryBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigate: SplashNavigate,
    viewModel: DefaultViewModel = hiltViewModel()
) {
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
        viewModel.remoteConfig.start()
        delay(2000L)
        navigate.toHome()
    }
    SplashScreenContent(scale)
}

@Composable
fun SplashScreenContent(scale: Animatable<Float, AnimationVector1D>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        AppIcon(scale)
    }
}

@Composable
fun AppIcon(scale: Animatable<Float, AnimationVector1D>) {
    Box(
        modifier = Modifier
            .scale(scale.value)
            .size(400.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.launcher_playstore),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp),
            contentScale = ContentScale.Crop
        )
    }
}
