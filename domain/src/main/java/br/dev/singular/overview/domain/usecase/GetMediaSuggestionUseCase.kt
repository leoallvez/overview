package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.MediaSuggestion
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.IMediaRepository
import br.dev.singular.overview.domain.repository.ISuggestionRepository

interface IGetMediaSuggestionUseCase {
    suspend operator fun invoke(): UseCaseState<List<MediaSuggestion>>
}

class GetMediaSuggestionUseCase(
    private val mediaRepository: IMediaRepository,
    private val suggestionRepository: ISuggestionRepository
) : IGetMediaSuggestionUseCase {

    override suspend fun invoke(): UseCaseState<List<MediaSuggestion>> {
        return try {
            val mediaSuggestions = getSuggestions().map { suggestion ->
                val medias = mediaRepository.getByPath(suggestion.path).take(MAX_MEDIA)
                MediaSuggestion(suggestion, medias)
            }
            UseCaseState.Success(mediaSuggestions)
        } catch (e: Exception) {
            UseCaseState.Failure(FailType.Exception)
        }
    }

    private suspend fun getSuggestions(): List<Suggestion> {
        return suggestionRepository.getAll().sortedBy { it.order }
    }

    private companion object {
        const val MAX_MEDIA = 10
    }
}
