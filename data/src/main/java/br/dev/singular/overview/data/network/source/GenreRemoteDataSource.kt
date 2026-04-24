package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.ApiService
import com.haroldadmin.cnradapter.NetworkResponse
import timber.log.Timber
import javax.inject.Inject

interface IGenreRemoteDataSource {
    suspend fun getByMediaType(type: MediaDataType): List<GenreDataModel>
}

class GenreRemoteDataSource @Inject constructor(
    private val api: ApiService
) : IGenreRemoteDataSource {

    override suspend fun getByMediaType(type: MediaDataType) = try {
        when (val response = api.getGenres(mediaType = type.key)) {
            is NetworkResponse.Success -> response.body.genres
            else -> listOf()
        }
    } catch (e: Exception) {
        Timber.e(e)
        emptyList()
    }
}
