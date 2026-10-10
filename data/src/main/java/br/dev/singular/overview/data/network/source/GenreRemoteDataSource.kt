package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.ApiService
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

interface IGenreRemoteDataSource {
    suspend fun getByMediaType(type: MediaDataType): List<GenreDataModel>
}

class GenreRemoteDataSource @Inject constructor(
    private val api: ApiService
) : IGenreRemoteDataSource {

    /**
     * Fetches the genres in the device language along with their original (English) names.
     * When the original names can't be fetched, they are left empty.
     */
    override suspend fun getByMediaType(type: MediaDataType) = try {
        coroutineScope {
            val translated = async { fetch(type) }
            val originals = async { fetch(type, language = ORIGINAL_LANGUAGE) }
            translated.await().withOriginalNames(originals = originals.await())
        }
    } catch (e: Exception) {
        Timber.e(e)
        emptyList()
    }

    private suspend fun fetch(type: MediaDataType, language: String? = null) =
        when (val response = api.getGenres(mediaType = type.key, language = language)) {
            is NetworkResponse.Success -> response.body.genres
            else -> listOf()
        }

    private fun List<GenreDataModel>.withOriginalNames(
        originals: List<GenreDataModel>
    ): List<GenreDataModel> {
        val originalNames = originals.associate { it.id to it.name }
        return map { it.copy(originalName = originalNames[it.id].orEmpty()) }
    }

    private companion object {
        const val ORIGINAL_LANGUAGE = "en-US"
    }
}
