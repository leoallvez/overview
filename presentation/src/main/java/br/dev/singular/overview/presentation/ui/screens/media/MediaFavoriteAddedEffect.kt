package br.dev.singular.overview.presentation.ui.screens.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide

/**
 * An effect that notifies the user, through a snackbar, that a media was added to the favorites.
 *
 * @param favoriteAdded Whether the media has just been added to the favorites.
 * @param mediaTitle The title of the media, displayed as the snackbar message.
 * @param onShow Callback that receives the snackbar to be displayed.
 */
@Composable
internal fun MediaFavoriteAddedEffect(
    favoriteAdded: Boolean,
    mediaTitle: String,
    onShow: (UiSnackbarVisuals) -> Unit
) {
    val title = stringResource(R.string.favorite_added_title)

    LaunchedEffect(favoriteAdded) {
        if (favoriteAdded) {
            onShow(
                UiSnackbarVisuals.Close(
                    title = title,
                    message = mediaTitle,
                    icon = UiIconSource.vector(Lucide.Heart)
                )
            )
        }
    }
}
