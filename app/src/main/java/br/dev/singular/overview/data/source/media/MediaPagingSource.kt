package br.dev.singular.overview.data.source.media

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.util.PagingMediaResult
import retrofit2.HttpException
import java.io.IOException

class MediaPagingSource(
    private val _onRequest: suspend (page: Int) -> PagingMediaResult
) : PagingSource<Int, MediaEntity>() {

    override suspend fun load(params: LoadParams<Int>) = try {
        val response = _onRequest(params.key ?: STARTING_PAGE_INDEX)
        loadResult(response)
    } catch (e: IOException) {
        LoadResult.Error(e)
    } catch (e: HttpException) {
        LoadResult.Error(e)
    }

    private fun loadResult(response: PagingMediaResult) = if (response.data == null) {
        LoadResult.Error(IOException())
    } else {
        val data = response.data
        if (data.results.isNotEmpty()) {
            LoadResult.Page(
                data = data.results,
                prevKey = data.prevPage(),
                nextKey = data.nextPage()
            )
        } else {
            LoadResult.Error(IOException())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MediaEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
