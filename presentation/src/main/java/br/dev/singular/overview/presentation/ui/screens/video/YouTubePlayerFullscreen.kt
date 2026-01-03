package br.dev.singular.overview.presentation.ui.screens.video

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import br.dev.singular.overview.presentation.ui.utils.setFullscreen
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * A composable that displays a YouTube video in fullscreen.
 *
 * @param tagPath The path for analytics tagging.
 * @param videoKey The key of the YouTube video to be played.
 * @param setEdgeToEdge A callback to enable or disable edge-to-edge display.
 * @param onBack A callback to be invoked when the user navigates back.
 */
@Composable
fun YouTubePlayerFullscreen(
    tagPath: String= TagPlayer.PATH,
    videoKey: String,
    setEdgeToEdge: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    TrackScreenView(tagPath)
    val context = LocalContext.current
    val activity = remember { context as? Activity }

    val setFullscreen = { enabled: Boolean ->
        setEdgeToEdge(enabled)
        activity?.setFullscreen(isFullscreen = enabled)
    }

    DisposableEffect(Unit) {
        setFullscreen(true)
        onDispose {
            setFullscreen(false)
        }
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
                TagManager.logClick(tagPath, TagCommon.Detail.CLOSE)
                onBack()
            }
        )
    }
}
