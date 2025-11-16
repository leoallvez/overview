package br.dev.singular.overview.ui.video

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.viewinterop.AndroidView
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.tagging.params.TagPlayer
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.util.YouTubePlayerListener
import br.dev.singular.overview.util.setFullscreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayerFullscreen(
    videoKey: String,
    onBackstackClick: () -> Unit
) {
    TrackScreenView(TagPlayer.PATH)
    val context = LocalContext.current
    val activity = remember { context as? Activity }
    val systemUiController = rememberSystemUiController()

    val exitFullscreen: () -> Unit = {
        activity.setFullscreen(isFullscreen = false)
        onBackstackClick.invoke()
    }

    BackHandler {
        exitFullscreen()
    }

    LaunchedEffect(Unit) {
        systemUiController.isStatusBarVisible = false
        activity.setFullscreen(isFullscreen = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                YouTubePlayerView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(android.graphics.Color.BLACK)
                    enableAutomaticInitialization = false
                    initialize(YouTubePlayerListener(videoKey))
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        UiIconButton(
            iconStyle = UiIconStyle(
                source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                descriptionRes = R.string.backstack_icon,
            ),
            background = Color.Gray.copy(alpha = 0.1f),
            modifier = Modifier.align(Alignment.TopStart).padding(
                start = dimensionResource(R.dimen.spacing_4x),
                top = dimensionResource(R.dimen.spacing_15x),
            ),
            onClick = {
                TagManager.logClick(TagPlayer.PATH, TagCommon.Detail.CLOSE)
                exitFullscreen()
            }
        )
    }
}
