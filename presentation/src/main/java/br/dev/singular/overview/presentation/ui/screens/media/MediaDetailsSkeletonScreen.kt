package br.dev.singular.overview.presentation.ui.screens.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.genre.UiGenreListSkeleton
import br.dev.singular.overview.presentation.ui.components.media.UiMediaListSkeleton
import br.dev.singular.overview.presentation.ui.components.person.UiPersonListSkeleton
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.text.UiTextSkeleton
import br.dev.singular.overview.presentation.ui.components.video.UiVideoListSkeleton
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview

@Composable
internal fun MediaDetailsSkeletonScreen(
    modifier: Modifier = Modifier,
    tagPath: String = "",
) {
    val horizontalPadding = dimensionResource(R.dimen.spacing_4x)

    TrackScreenView(tagPath, status = TagStatus.LOADING)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_2x)),
        contentPadding = PaddingValues(bottom = horizontalPadding),
        userScrollEnabled = false,
    ) {
        item {
            Box(Modifier.fillMaxWidth()) {
                UiShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(
                            RoundedCornerShape(
                                size = dimensionResource(R.dimen.corner_width)
                            )
                        )
                )
                UiShimmerBox(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontalPadding)
                        .size(width = 200.dp, height = 24.dp)
                )
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
            ) {
                UiShimmerBox(
                    modifier = Modifier
                        .size(width = 120.dp, height = 20.dp)
                )
                UiShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.spacing_12x))
                )
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
            ) {
                repeat(3) {
                    UiTextSkeleton(modifier = Modifier.width(150.dp))
                }
            }
        }

        item {
            UiGenreListSkeleton(
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            )
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
            ) {
                UiTextSkeleton(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(bottom = dimensionResource(R.dimen.spacing_2x))
                )
                UiTextSkeleton(modifier = Modifier.fillMaxWidth(0.9f))
                UiTextSkeleton(modifier = Modifier.fillMaxWidth(0.85f))
                UiTextSkeleton(modifier = Modifier.fillMaxWidth(0.7f))
            }
        }

        item {
            UiVideoListSkeleton(
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            )
        }

        item {
            UiPersonListSkeleton(
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            )
        }

        item {
            UiPersonListSkeleton(
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            )
        }

        item {
            UiMediaListSkeleton(
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            )
        }
    }
}

@UiScreenPreview
@Composable
internal fun MediaDetailsSkeletonScreenPreview() {
    UiShimmerProvider {
        MediaDetailsSkeletonScreen()
    }
}
