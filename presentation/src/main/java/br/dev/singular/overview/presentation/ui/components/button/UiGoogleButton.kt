package br.dev.singular.overview.presentation.ui.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
internal fun UiGoogleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    UiButtonBase(
        text = stringResource(R.string.sign_in_with_google),
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        style = UiGoogleButtonStyle(),
    ) {
        Image(
            painter = painterResource(R.drawable.google_logo),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.spacing_5x))
        )
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
