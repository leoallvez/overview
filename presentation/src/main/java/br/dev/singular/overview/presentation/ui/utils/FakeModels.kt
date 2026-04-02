package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.StreamingUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

private const val BASE_TITLE = "The Equinox"
private const val LONG_TITLE = "The Equinox: Warriors Against the Infinite Swarm from the Dark Expanse"

@Composable
fun fakeMedias(
    count: Int = 10,
    withLongText: Boolean = false
): ImmutableList<MediaUiModel> {

    val baseModel = MediaUiModel(
        id = 0,
        title = BASE_TITLE,
        posterURL = "https://imagens.com/movie.jpg",
        type = MediaUiType.MOVIE,
        previewDrawableRes = R.drawable.sample_poster
    )

    return buildList {
        repeat(count) { index ->
            val title = if (index % 2 == 1 || withLongText) {
                LONG_TITLE
            } else {
                "$BASE_TITLE ${index + 1}"
            }
            add(
                baseModel.copy(
                    id = index.toLong(),
                    title = title,
                    uiId = UUID.randomUUID().toString()
                )
            )
        }
    }.toImmutableList()
}

@Composable
fun ImmutableList<MediaUiModel>.collectAsFakeLazyPagingItems(): LazyPagingItems<MediaUiModel> {
    val pagingData = PagingData.from(
        data = this,
        sourceLoadStates = LoadStates(
            refresh = LoadState.NotLoading(endOfPaginationReached = false),
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true)
        )
    )

    val pagingDataFlow = flowOf(pagingData)
    return pagingDataFlow.collectAsLazyPagingItems()
}

@Composable
fun fakeStreaming(count: Int = 10): ImmutableList<StreamingUiModel> {
    return buildList {
        repeat(count) { index ->
            add(
                StreamingUiModel(
                    id = index.toLong(),
                    priority = index,
                    name = stringResource(R.string.lorem_ipsum),
                    logoURL = "https://imagens.com/streaming.jpg",
                    previewDrawableRes = R.drawable.scifi_stream
                )
            )
        }
    }.toImmutableList()
}
