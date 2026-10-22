package br.dev.singular.overview.presentation.ui.components.user

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.person.UiPersonAvatar
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A clickable circular avatar representing the signed-in user.
 *
 * @param url The URL of the user's profile picture.
 * @param modifier The modifier to be applied to the avatar.
 * @param previewDrawableRes The drawable displayed instead of the remote picture in previews.
 * @param size The diameter of the avatar.
 * @param onClick The callback to be invoked when the avatar is clicked.
 */
@Composable
internal fun UiUserAvatar(
    url: String,
    modifier: Modifier = Modifier,
    @DrawableRes
    previewDrawableRes: Int? = null,
    size: Dp = dimensionResource(R.dimen.spacing_9x),
    onClick: () -> Unit
) {
    val description = stringResource(R.string.profile)

    UiPersonAvatar(
        url = url,
        modifier = modifier
            .clip(CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description },
        previewDrawableRes = previewDrawableRes,
        size = size
    )
}

@UiComponentPreview
@Composable
internal fun UiUserAvatarPreview() {
    UiUserAvatar(
        url = "https://imagens.com/user.jpg",
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        previewDrawableRes = R.drawable.sample_profile,
        onClick = {}
    )
}
