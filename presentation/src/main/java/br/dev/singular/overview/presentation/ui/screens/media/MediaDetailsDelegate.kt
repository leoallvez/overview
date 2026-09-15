package br.dev.singular.overview.presentation.ui.screens.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IObserveFavoritesUseCase
import br.dev.singular.overview.domain.usecase.media.IToggleFavoriteUseCase
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Delegate responsible for shared logic across Media Details screens (Movies and TV Shows).
 * It handles operations like toggling the "liked" status and managing catalog selection.
 */
interface IMediaDetailsDelegate {
    /**
     * Observes whether a media item is liked, following the favorites and the session changes.
     *
     * @param id The ID of the media item.
     * @param type The type of the media item, as movies and TV shows share the same IDs.
     * @return A flow that emits true while the media is liked, false otherwise.
     */
    fun observeIsLiked(id: Long, type: MediaType): Flow<Boolean>

    /**
     * Toggles the "liked" status of the given [media] and persists the change.
     *
     * @param media The media item to toggle.
     * @return The new liked status after the change, or a failure when it could not be
     * changed (e.g. the user is not signed in).
     */
    suspend fun toggleLike(media: Media): UseCaseState<Boolean>

    /**
     * Updates the current query state with the selected [catalog].
     *
     * @param catalog The catalog to be selected.
     */
    suspend fun selectCatalog(catalog: CatalogUiModel)
}

class MediaDetailsDelegate @Inject constructor(
    private val favoritesUseCase: IObserveFavoritesUseCase,
    private val queryUseCase: ICatalogQueryStateUseCase,
    private val favoriteUseCase: IToggleFavoriteUseCase,
) : IMediaDetailsDelegate {

    override fun observeIsLiked(id: Long, type: MediaType): Flow<Boolean> {
        return favoritesUseCase()
            .map { favorites -> favorites.any { it.id == id && it.type == type } }
            .distinctUntilChanged()
    }

    override suspend fun toggleLike(media: Media) = favoriteUseCase(media)

    override suspend fun selectCatalog(catalog: CatalogUiModel) {
        val currentQuery = queryUseCase.get() ?: QueryState()
        queryUseCase.save(currentQuery.copy(catalog = catalog.toDomain()))
    }

}
