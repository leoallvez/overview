package br.dev.singular.overview.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor

@Composable
fun UiAnimatedVisibility(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it }
        ) + fadeIn(
            animationSpec = tween(200)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it }
        ) + fadeOut(
            targetAlpha = 0.1f,
            animationSpec = tween(200)
        )
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0x00000000)
@Composable
fun PreviewUiAnimatedVisibility() {

    var isVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UiChip(
            activated = !isVisible,
            lowlightColor = DefaultTextColor,
            text = if (isVisible) "Hide" else "Show",
            shape = RoundedCornerShape(20)
        ) {
            isVisible = !isVisible
        }

        UiAnimatedVisibility(visible = isVisible) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(70.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "UI Animated Visibility")
                }
            }
        }
    }
}
