package br.com.deepbyte.overview.data.source.discover

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.source.DataResult
import retrofit2.HttpException
import java.io.IOException

typealias DiscoverResult = DataResult<PagingResponse<MediaItem>>

class DiscoverPagingSource(
    private val _mediaType: String,
    val _onRequest: suspend (page: Int) -> DiscoverResult
) : PagingSource<Int, MediaItem>() {

    override suspend fun load(params: LoadParams<Int>) = try {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        val response = _onRequest(pageNumber)
        loadResult(response)
    } catch (e: IOException) {
        LoadResult.Error(e)
    } catch (e: HttpException) {
        LoadResult.Error(e)
    }

    private fun loadResult(response: DiscoverResult) = if (response.data == null) {
        LoadResult.Error(IOException())
    } else {
        val data = response.data
        if (data.results.isNotEmpty()) {
            setMediaType(data, _mediaType)
            LoadResult.Page(
                data = data.results,
                prevKey = data.prevPage(),
                nextKey = data.nextPage()
            )
        } else {
            LoadResult.Error(IOException())
        }
    }

    private fun setMediaType(response: PagingResponse<MediaItem>?, mediaType: String) {
        response?.results?.forEach { it.type = mediaType }
    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}