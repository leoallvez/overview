package br.com.deepbyte.take.ui.splash

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.take.ui.ScreenNav
import br.com.deepbyte.take.ui.TrackScreenView
import br.com.deepbyte.take.ui.theme.AccentColor
import br.com.deepbyte.take.ui.theme.PrimaryBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit, viewModel: SplashViewModel = hiltViewModel()) {

    TrackScreenView(screen = ScreenNav.Splash, tracker = viewModel.analyticsTracker)

    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(
                durationMillis = 900,
                easing = {
                    OvershootInterpolator(5f).getInterpolation(it)
                }
            )
        )
        delay(1500L)
        onNavigateToHome.invoke()
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
            .size(350.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(350.dp), onDraw = {
            drawCircle(
                brush = Brush.horizontalGradient(
                    colors = listOf(AccentColor, Color.White),
                    startX = 0.dp.toPx(),
                    endX = 800.dp.toPx()
                )
            )
        })
        Canvas(modifier = Modifier.size(250.dp), onDraw = {
            drawCircle(color = Color.Black)
        })
    }
}
