package br.dev.singular.overview.presentation.ui.components.prediction

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.AffinityLevel
import br.dev.singular.overview.presentation.model.PredictionUiModel
import br.dev.singular.overview.presentation.ui.components.button.UiActionButton
import br.dev.singular.overview.presentation.ui.components.button.style.UiActionButtonStyle
import br.dev.singular.overview.presentation.ui.components.button.style.UiFilledButtonStyle
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTextSkeleton
import br.dev.singular.overview.presentation.ui.theme.AttentionColor
import br.dev.singular.overview.presentation.ui.theme.BorderColor
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.SubtitleTextColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles

private const val SKELETON_SHORT_LINE_FRACTION = 0.7f

/**
 * A composable that displays an AI taste prediction card with loading, idle, affinity, no data,
 * login required or error states.
 *
 * @param model The [PredictionUiModel] containing the data to display.
 * @param modifier The modifier to be applied to this component.
 * @param onPredict Callback invoked when the user asks to see the prediction result or retry.
 * @param onLogin Callback invoked when the user asks to sign in to get a prediction.
 */
@Composable
internal fun UiAiPredictionCard(
    model: PredictionUiModel,
    modifier: Modifier = Modifier,
    onPredict: (isTryAgain: Boolean) -> Unit = {},
    onLogin: () -> Unit = {},
) {
    if (model is PredictionUiModel.Loading) {
        UiAiPredictionCardSkeleton(modifier = modifier)
    } else {
        val title = when (model) {
            is PredictionUiModel.Unavailable -> R.string.taste_prediction_no_data
            is PredictionUiModel.LoginRequired -> R.string.login_required_title
            is PredictionUiModel.Error -> R.string.taste_prediction_error_title
            else -> R.string.taste_prediction_title
        }

        val description = when (model) {
            is PredictionUiModel.Idle -> R.string.taste_prediction_idle
            is PredictionUiModel.Predicted -> model.level.description
            is PredictionUiModel.Unavailable -> R.string.taste_prediction_unavailable
            is PredictionUiModel.LoginRequired -> R.string.taste_prediction_login_description
            is PredictionUiModel.Error -> R.string.taste_prediction_error_description
            else -> R.string.taste_prediction_idle
        }

        PredictionCardContainer(
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics(mergeDescendants = true) {},
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_4x)
                )
            ) {
                when (model) {
                    is PredictionUiModel.Idle -> IconIndicator(icon = Lucide.Sparkles)
                    is PredictionUiModel.Predicted -> AffinityIndicator(model)
                    is PredictionUiModel.Unavailable -> IconIndicator(icon = Lucide.Heart)
                    is PredictionUiModel.LoginRequired -> IconIndicator(icon = Lucide.Lock)
                    is PredictionUiModel.Error -> IconIndicator(icon = Lucide.CircleAlert)
                    else -> {}
                }

                PredictionTexts(
                    title = title,
                    description = description,
                    showSparklesIcon = model !is PredictionUiModel.Idle,
                    modifier = Modifier.weight(1f)
                )
            }

            when (model) {
                is PredictionUiModel.Idle -> {
                    UiActionButton(
                        text = stringResource(R.string.taste_prediction_action),
                        style = UiActionButtonStyle(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        onPredict(false)
                    }
                }

                is PredictionUiModel.Error -> {
                    UiActionButton(
                        text = stringResource(R.string.btn_try_again),
                        style = UiActionButtonStyle(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        onPredict(true)
                    }
                }

                is PredictionUiModel.LoginRequired -> {
                    UiActionButton(
                        text = stringResource(R.string.login),
                        style = UiFilledButtonStyle(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        onLogin()
                    }
                }

                else -> {}
            }
        }
    }
}

/**
 * A skeleton loader for [UiAiPredictionCard].
 *
 * @param modifier The modifier to be applied to this component.
 */
@Composable
internal fun UiAiPredictionCardSkeleton(
    modifier: Modifier = Modifier
) {
    PredictionCardContainer(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {},
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_4x)
            )
        ) {
            UiShimmerBox(
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_20x)),
                shape = CircleShape
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
            ) {
                UiTextSkeleton()
                UiTextSkeleton(
                    modifier = Modifier.fillMaxWidth(SKELETON_SHORT_LINE_FRACTION)
                )
            }
        }
    }
}

@Composable
private fun PredictionCardContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(
        size = dimensionResource(R.dimen.corner_width)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                style = UiBorderStyle(
                    shape = shape
                )
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_5x)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4x)),
            content = content
        )
    }
}

@Composable
private fun AffinityIndicator(
    model: PredictionUiModel.Predicted,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(dimensionResource(R.dimen.spacing_20x))
    ) {
        CircularProgressIndicator(
            progress = { model.progress },
            modifier = Modifier
                .fillMaxSize()
                .clearAndSetSemantics {},
            color = model.level.color,
            trackColor = BorderColor,
            strokeWidth = dimensionResource(R.dimen.prediction_indicator_stroke),
            strokeCap = StrokeCap.Round
        )
        UiText(
            text = "${model.affinityPercentage}%",
            style = MaterialTheme.typography.titleMedium,
            isBold = true
        )
    }
}

@Composable
private fun IconIndicator(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensionResource(R.dimen.spacing_20x))
            .background(
                color = HighlightColor.copy(alpha = 0.12f),
                shape = CircleShape
            )
    ) {
        UiIcon(
            source = UiIconSource.vector(icon),
            color = HighlightColor,
            modifier = Modifier.size(dimensionResource(R.dimen.spacing_10x))
        )
    }
}

@Composable
private fun PredictionTexts(
    @StringRes title: Int,
    @StringRes description: Int,
    showSparklesIcon: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
        ) {
            if (showSparklesIcon) {
                UiIcon(
                    source = UiIconSource.vector(Lucide.Sparkles),
                    color = HighlightColor,
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_4x))
                )
            }
            UiText(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                color = DefaultTextColor,
                textAlign = TextAlign.Start,
                isBold = true,
                maxLines = 1
            )
        }
        UiText(
            text = stringResource(description),
            style = MaterialTheme.typography.bodyMedium,
            color = SubtitleTextColor,
            textAlign = TextAlign.Start,
            maxLines = Int.MAX_VALUE
        )
    }
}

private val AffinityLevel.color: Color
    get() = when (this) {
        AffinityLevel.HIGH -> HighlightColor
        AffinityLevel.MEDIUM -> AttentionColor
        AffinityLevel.LOW -> LowlightColor
    }

private val AffinityLevel.description: Int
    @StringRes get() = when (this) {
        AffinityLevel.HIGH -> R.string.taste_prediction_high
        AffinityLevel.MEDIUM -> R.string.taste_prediction_medium
        AffinityLevel.LOW -> R.string.taste_prediction_low
    }

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardIdlePreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.Idle,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardHighPreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.Predicted(
            affinityPercentage = 90
        ),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardMediumPreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.Predicted(
            affinityPercentage = 50
        ),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardLowPreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.Predicted(
            affinityPercentage = 10
        ),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardUnavailablePreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.Unavailable,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardLoginRequiredPreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.LoginRequired,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardErrorPreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.Error,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardLoadingPreview() {
    UiAiPredictionCard(
        model = PredictionUiModel.Loading,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardSkeletonPreview() {
    UiAiPredictionCardSkeleton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}
