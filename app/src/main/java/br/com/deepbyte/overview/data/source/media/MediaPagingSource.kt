package br.com.deepbyte.overview.data.source.media

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult
import retrofit2.HttpException
import java.io.IOException

typealias MediaResult = DataResult<PagingResponse<Media>>

class MediaPagingSource(
    private val _onRequest: suspend (page: Int) -> MediaResult
) : PagingSource<Int, Media>() {

    override suspend fun load(params: LoadParams<Int>) = try {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        val response = _onRequest(pageNumber)
        loadResult(response)
    } catch (e: IOException) {
        LoadResult.Error(e)
    } catch (e: HttpException) {
        LoadResult.Error(e)
    }

    private fun loadResult(response: MediaResult) = if (response.data == null) {
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

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
