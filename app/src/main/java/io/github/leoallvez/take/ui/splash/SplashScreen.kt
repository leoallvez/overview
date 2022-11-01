package io.github.leoallvez.take.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.ui.ScreenNav
import io.github.leoallvez.take.ui.TrackScreenView
import io.github.leoallvez.take.ui.theme.BlueTake
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(logger: Logger, onNavigateToHome: () -> Unit) {

    TrackScreenView(screen = ScreenNav.Splash, logger)

    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
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
        Box(
            modifier = Modifier
                .scale(scale.value)
                .width(250.dp)
        ) {
            Text(
                text = stringResource(R.string.app_global_name),
                style = MaterialTheme.typography.overline,
                fontSize = 150.sp,
                color = BlueTake
            )
            DebugFlavorTag(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}

@Composable
fun DebugFlavorTag(modifier: Modifier) {
    if (BuildConfig.DEBUG) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(5.dp),
            modifier = modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner)))
        ) {
            Text(
                text = BuildConfig.FLAVOR.capitalize(Locale.current),
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(BlueTake)
                    .padding(horizontal = 15.dp)
            )
        }
    }
}
