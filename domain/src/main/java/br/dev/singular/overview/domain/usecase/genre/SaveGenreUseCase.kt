package br.dev.singular.overview.domain.usecase.genre

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MediaTypeGenres
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface ISaveGenreUseCase {
    suspend operator fun invoke(): UseCaseState<Unit>
}

class SaveGenreUseCase(
    private val setter: Update<MediaTypeGenres>,
    private val getter: GetByParam<List<Genre>, MediaType>
) : ISaveGenreUseCase {

    override suspend fun invoke() = runSafely {
        save(type = MediaType.TV)
        save(type = MediaType.MOVIE)
    }

    private suspend fun save(type: MediaType) {
        val genres = getter.getByParam(param = type)
        setter.update(item = MediaTypeGenres(type = type, genres = genres))
    }
}
