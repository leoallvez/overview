package br.dev.singular.overview.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.navigation.wrappers.ISplashNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.PrimaryBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigate: ISplashNavigate,
    viewModel: SplashViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Splash, tracker = viewModel.analyticsTracker)

    val scale = remember { Animatable(0f) }
    LaunchedEffect(key1 = Unit) {
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
        navigate.toStreamingExplore(json = viewModel.getSelectedStreamingJson())
    }
    SplashScreenContent(scale)
}

@Composable
fun SplashScreenContent(scale: Animatable<Float, AnimationVector1D>) {
    Box(
        modifier = Modifier.fillMaxSize().background(PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        AppIcon(scale)
    }
}

@Composable
fun AppIcon(scale: Animatable<Float, AnimationVector1D>) {
    val colors = listOf(Color.Cyan, AccentColor, AccentColor)
    val brush = Brush.linearGradient(colors = colors)
    val onDraw: DrawScope.() -> Unit = {
        drawCircle(brush = brush)
    }
    Box(
        modifier = Modifier.scale(scale.value).size(295.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(575.dp), onDraw)
        Canvas(modifier = Modifier.size(275.dp), onDraw = {
            drawCircle(color = Color.Black)
        })
        Canvas(modifier = Modifier.size(175.dp), onDraw)
    }
}
