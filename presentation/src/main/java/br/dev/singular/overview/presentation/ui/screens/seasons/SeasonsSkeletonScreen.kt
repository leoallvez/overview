package br.dev.singular.overview.presentation.ui.screens.seasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.episodes.chip.UiWatchedChipSkeleton
import br.dev.singular.overview.presentation.ui.components.episodes.item.UiEpisodeItemSkeleton
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.text.UiTextSkeleton
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview

/**
 * A skeleton loader screen for [SeasonsScreen].
 *
 * @param tagPath The path for analytics tagging.
 */
@Composable
fun SeasonsSkeletonScreen(
    tagPath: String = ""
) {
    TrackScreenView(tagPath, status = TagStatus.LOADING)

    UiScaffold(
        modifier = Modifier,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.spacing_14x)),
                verticalArrangement = Arrangement.Center
            ) {
                UiShimmerBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp),
                    shape = RoundedCornerShape(4.dp)
                )
                UiShimmerBox(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(180.dp)
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            userScrollEnabled = false,
            contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.spacing_20x))
        ) {
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.spacing_2x)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x)),
                    userScrollEnabled = false
                ) {
                    items(4) {
                        UiWatchedChipSkeleton()
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensionResource(R.dimen.spacing_2x),
                            bottom = dimensionResource(R.dimen.spacing_4x)
                        ),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4x))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UiTextSkeleton(modifier = Modifier.width(160.dp))
                        UiTextSkeleton(modifier = Modifier.width(90.dp))
                    }

                    UiShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.spacing_1x))
                    )
                }
            }

            items(15) {
                UiEpisodeItemSkeleton(
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_2x))
                )
            }
        }
    }
}

@UiScreenPreview
@Composable
internal fun SeasonsSkeletonScreenPreview() {
    UiShimmerProvider {
        SeasonsSkeletonScreen()
    }
}
