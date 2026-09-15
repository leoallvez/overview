package br.dev.singular.overview.presentation.ui.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.button.style.UiGoogleButtonStyle
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A branded Google Sign-in button.
 *
 * @param modifier The modifier to be applied to the button.
 * @param enabled Whether the button is interactive and enabled.
 * @param loading Whether the sign in is in progress. While loading, a progress indicator
 * replaces the Google logo and clicks are ignored.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
internal fun UiGoogleButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val style = UiGoogleButtonStyle()

    UiButtonBase(
        enabled = enabled,
        text = stringResource(R.string.sign_in_with_google),
        onClick = { if (!loading) onClick() },
        modifier = modifier,
        shape = CircleShape,
        style = style,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_5x)),
                color = style.colors.textColor(enabled),
                strokeWidth = dimensionResource(R.dimen.border_width) * 2
            )
        } else {
            Image(
                painter = painterResource(R.drawable.google_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.spacing_5x))
                    .alpha(if (enabled) 1f else 0.5f)
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_3x)))
    }
}

@UiComponentPreview
@Composable
internal fun UiGoogleButtonPreview() {
    UiGoogleButton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiGoogleButtonLoadingPreview() {
    UiGoogleButton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        loading = true,
        onClick = {}
    )
}
