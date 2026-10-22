package br.dev.singular.overview.presentation.ui.screens.person

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.media.UiMediaListSkeleton
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.text.UiTextSkeleton
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.defaultBackground

@Composable
fun PersonDetailsSkeletonScreen(
    tagPath: String = ""
) {
    TrackScreenView(tagPath, status = TagStatus.LOADING)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .defaultBackground()
            .verticalScroll(rememberScrollState(), enabled = false)
    ) {
        PersonToolBarSkeleton()
        PersonBodySkeleton()
    }
}

@Composable
private fun PersonToolBarSkeleton() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(292.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_width)))
            .padding(top = dimensionResource(R.dimen.spacing_4x))
            .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    ) {
        UiShimmerBox(
            modifier = Modifier
                .size(dimensionResource(R.dimen.avatar_large))
                .clip(CircleShape)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun PersonBodySkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultBackground()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_4x),
                    vertical = dimensionResource(R.dimen.spacing_2x)
                )
        ) {
            // Name
            UiShimmerBox(
                modifier = Modifier
                    .width(200.dp)
                    .height(32.dp)
                    .padding(vertical = dimensionResource(R.dimen.spacing_1x))
            )

            // Dates
            UiTextSkeleton(
                modifier = Modifier
                    .width(150.dp)
                    .padding(vertical = dimensionResource(R.dimen.spacing_1x))
            )

            // Place of birth
            Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_1x))) {
                UiTextSkeleton(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(vertical = dimensionResource(R.dimen.spacing_1x))
                )
                UiTextSkeleton(
                    modifier = Modifier
                        .width(180.dp)
                        .padding(vertical = dimensionResource(R.dimen.spacing_1x))
                )
            }

            // Biography
            Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_2x))) {
                UiShimmerBox(
                    modifier = Modifier
                        .width(100.dp)
                        .height(24.dp)
                        .padding(vertical = dimensionResource(R.dimen.spacing_1x))
                )
                repeat(4) {
                    UiTextSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(if (it == 3) 0.7f else 1f)
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        // Participation (Movies)
        UiMediaListSkeleton(
            contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
            itemCount = 5
        )

        // Participation (TV Shows)
        UiMediaListSkeleton(
            contentPadding = PaddingValues(
                start = dimensionResource(R.dimen.spacing_4x),
                top = dimensionResource(R.dimen.spacing_3x)
            ),
            itemCount = 5
        )
    }
}

@UiScreenPreview
@Composable
internal fun PersonDetailsSkeletonScreenPreview() {
    UiShimmerProvider {
        PersonDetailsSkeletonScreen()
    }
}
