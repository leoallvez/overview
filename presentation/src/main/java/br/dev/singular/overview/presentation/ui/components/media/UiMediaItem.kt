package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.utils.fakeMedias

/**
 * A composable that displays a media item, including its poster and title.
 *
 * @param model The [MediaUiModel] to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the item is clicked.
 */
@Composable
fun UiMediaItem(
    model: MediaUiModel,
    modifier: Modifier = Modifier,
    onClick: (MediaUiModel) -> Unit = {}
) {
    val width = dimensionResource(R.dimen.poster_width)

    Column(
        Modifier
            .clickable { onClick.invoke(model) }
            .semantics(mergeDescendants = true) {}
            .width(width)
    ) {
        UiImage(
            url = model.posterURL,
            style = UiImageStyle(
                borderStyle = UiBorderStyle(visible = true),
                errorDrawableRes = R.drawable.error_poster_placeholder,
                previewDrawableRes = model.previewDrawableRes
            ),
            modifier = modifier
                .size(width, height = dimensionResource(R.dimen.poster_height))
        )
        UiText(
            text = model.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_1x)),
            style = MaterialTheme.typography.bodySmall,
            isBold = true
        )
    }
}

/**
 * A skeleton placeholder for [UiMediaItem] to be used during loading states.
 *
 * This composable mimics the structure of [UiMediaItem] using shimmer effects
 * to provide a better user experience while data is being fetched.
 */
@Composable
fun UiMediaItemSkeleton() {
    Column(
        Modifier.width(dimensionResource(R.dimen.poster_width))
    ) {
        UiShimmerBox(
            modifier = Modifier
                .height(height = dimensionResource(R.dimen.poster_height))
                .fillMaxWidth()
        )
        UiShimmerBox(
            modifier = Modifier
                .height(dimensionResource(R.dimen.spacing_8x))
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_1x))
        )
    }
}

@Preview(name = "Short Title")
@Composable
internal fun UiMediaItemPreview() {
    UiMediaItem(
        model = fakeMedias().first()
    )
}

@Preview(name = "Long Title")
@Composable
internal fun UiMediaWithLongTitlePreview() {
    UiMediaItem(model = fakeMedias(withLongText = true).first())
}

@Preview(name = "Skeleton")
@Composable
internal fun UiMediaItemSkeletonPreview() {
    UiMediaItemSkeleton()
}
