package br.dev.singular.overview.presentation.ui.components.navigation.bottom

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State

/**
 * Holds the animated scale values of a bottom navigation item.
 *
 * @param iconScale The animated scale applied to the item icon.
 * @param labelScale The animated scale applied to the item label.
 */
@Stable
internal class UiBottomNavigationScaleState(
    val iconScale: State<Float>,
    val labelScale: State<Float>,
)

/**
 * Contains the default values used by [UiBottomNavigation].
 */
internal object UiBottomNavigationDefaults {
    private const val SELECTED_ICON_SCALE = 1.15f
    private const val UNSELECTED_ICON_SCALE = 1.0f
    private const val SELECTED_LABEL_SCALE = 1.1f
    private const val UNSELECTED_LABEL_SCALE = 1.0f

    private val SelectedScaleAnimationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )

    private val UnselectedScaleAnimationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )

    /**
     * Animates the icon and label scales of a bottom navigation item based on its selection.
     *
     * @param selected Whether the item is the currently selected one.
     * @return The [UiBottomNavigationScaleState] holding the animated scales.
     */
    @Composable
    fun animateNavScales(selected: Boolean): UiBottomNavigationScaleState {
        val animationSpec = if (selected) {
            SelectedScaleAnimationSpec
        } else {
            UnselectedScaleAnimationSpec
        }
        val iconScale = animateFloatAsState(
            targetValue = if (selected) SELECTED_ICON_SCALE else UNSELECTED_ICON_SCALE,
            animationSpec = animationSpec,
            label = "BottomNavIconScale",
        )
        val labelScale = animateFloatAsState(
            targetValue = if (selected) SELECTED_LABEL_SCALE else UNSELECTED_LABEL_SCALE,
            animationSpec = animationSpec,
            label = "BottomNavLabelScale",
        )
        return UiBottomNavigationScaleState(
            iconScale = iconScale,
            labelScale = labelScale,
        )
    }
}
