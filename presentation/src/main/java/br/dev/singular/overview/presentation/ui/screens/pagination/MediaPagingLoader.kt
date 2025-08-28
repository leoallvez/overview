package br.dev.singular.overview.presentation.ui.screens.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import java.io.IOException

class MediaPagingLoader(
    private val onFetching: suspend (page: Int) -> UseCaseState<Page<Media>>
) : PagingSource<Int, Media>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> {
        val currentPage = params.key ?: FIRST_PAGE

        val result = runCatching {
            onFetching(currentPage)
        }.getOrElse {
            return LoadResult.Error(it)
        }

        return when (result) {
            is UseCaseState.Success ->
                result.data.toLoadResult(currentPage)
            is UseCaseState.Failure ->
                LoadResult.Error(IOException())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? =
        state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.let { page ->
                page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
            }
        }

    private fun Page<Media>.toLoadResult(currentPage: Int): LoadResult.Page<Int, Media> {
        return LoadResult.Page(
            data = items,
            prevKey = if (currentPage != FIRST_PAGE) currentPage - 1 else null,
            nextKey = if (!isLastPage) currentPage + 1 else null
        )
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}