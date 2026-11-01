package br.dev.singular.overview.presentation.ui.components.progress

import androidx.annotation.DimenRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.ProgressTrackColor
import br.dev.singular.overview.presentation.ui.theme.WarningColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A horizontal bar that fills its width proportionally to a progress value.
 *
 * @param progress The progress between 0 and 1. Values outside this range are clamped.
 * @param modifier The modifier to be applied to this component.
 * @param heightRes The dimension resource for the bar height.
 * @param trackColor The color of the unfilled track.
 * @param progressColor The color of the filled portion.
 */
@Composable
internal fun UiProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    @DimenRes heightRes: Int = R.dimen.progress_bar_height,
    trackColor: Color = ProgressTrackColor,
    progressColor: Color = HighlightColor
) {
    val fraction = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(heightRes))
            .background(trackColor)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(fraction, 0f..1f)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .fillMaxHeight()
                .background(progressColor)
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiProgressBarPreview() {
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4x))
    ) {
        UiProgressBar(progress = 0f)
        UiProgressBar(progress = 0.08f)
        UiProgressBar(progress = 0.5f)
        UiProgressBar(progress = 1f)
        UiProgressBar(
            progress = 0.5f,
            heightRes = R.dimen.spacing_1x,
            progressColor = WarningColor
        )
    }
}
