package io.github.leoallvez.take.data.source.discover

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.leoallvez.take.data.api.response.DiscoverResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.DataResult.Success
import retrofit2.HttpException
import java.io.IOException

class DiscoverPagingSource(
    private val _providerId: Long,
    private val _source: IDiscoverRemoteDataSource
) : PagingSource<Int, MediaItem>() {

    override suspend fun load(params: LoadParams<Int>) = try {
        val pageNumber = params.key ?: 1
        loadResult(response = _source.discoverOnTvByProvider(_providerId, pageNumber))
    } catch (e: IOException) {
        LoadResult.Error(e)
    } catch (e: HttpException) {
        LoadResult.Error(e)
    }

    private fun loadResult(response: DataResult<DiscoverResponse>) = if (response is Success) {

        val results = response.data?.results ?: listOf()
        val page = response.data?.page ?: 1

        LoadResult.Page(data = results, prevKey = null, nextKey = page + 1)
    } else {
        LoadResult.Error(Exception())
    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
