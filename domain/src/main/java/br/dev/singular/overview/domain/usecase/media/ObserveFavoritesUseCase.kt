package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Observe
import kotlinx.coroutines.flow.Flow

interface IObserveFavoritesUseCase {
    operator fun invoke(): Flow<List<Media>>
}

class ObserveFavoritesUseCase(
    private val observer: Observe<List<Media>>
) : IObserveFavoritesUseCase {

    override fun invoke(): Flow<List<Media>> = observer.observe()
}
