package br.dev.singular.overview.domain.usecase.suggestion

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IGetAllSuggestionsUseCase {
    suspend operator fun invoke(): UseCaseState<List<Suggestion>>
}

class GetAllSuggestionsUseCase(
    private val getterSuggestion: GetAll<Suggestion>,
    private val getterMedia: GetPage<Media, MediaParam>
) : IGetAllSuggestionsUseCase {

    override suspend fun invoke(): UseCaseState<List<Suggestion>> {
        return runCatching {
            getterSuggestion.getAll()
                .filter { it.isActive }
                .mapNotNull { suggestion ->
                    val medias = fetchMediasBySuggestion(suggestion)
                    if (medias.isNotEmpty()) suggestion.copy(medias = medias) else null
                }
        }.fold(
            onSuccess = { suggestions ->
                suggestions.ifEmpty { return UseCaseState.Failure(FailType.NothingFound) }
                UseCaseState.Success(suggestions.sortedBy { it.order })
            },
            onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
        )
    }

    private suspend fun fetchMediasBySuggestion(suggestion: Suggestion): List<Media> {
        return fetchMediasByKey(suggestion.key).let { medias ->
            when (suggestion.type) {
                MediaType.ALL -> medias
                else -> medias.map { it.copy(type = suggestion.type) }
            }
        }
    }

    private suspend fun fetchMediasByKey(key: String): List<Media> =
        getterMedia.getPage(param = MediaParam(key = key)).items.take(MAX_MEDIA)

    companion object {
        const val MAX_MEDIA = 20
    }
}
