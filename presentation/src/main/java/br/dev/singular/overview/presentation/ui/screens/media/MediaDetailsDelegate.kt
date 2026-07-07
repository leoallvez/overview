package br.dev.singular.overview.presentation.ui.screens.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.media.IMediaPersistenceUseCase
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import javax.inject.Inject

/**
 * Delegate responsible for shared logic across Media Details screens (Movies and TV Shows).
 * It handles operations like toggling the "liked" status and managing catalog selection.
 */
interface IMediaDetailsDelegate {
    /**
     * Checks if a media item is liked by its ID.
     *
     * @param id The ID of the media item.
     * @return True if the media is liked, false otherwise.
     */
    suspend fun getIsLiked(id: Long): Boolean

    /**
     * Toggles the "liked" status of the given [media] and persists the change.
     *
     * @param media The media item to toggle.
     * @return The new liked status after the change.
     */
    suspend fun toggleLike(media: Media): Boolean

    /**
     * Updates the current query state with the selected [catalog].
     *
     * @param catalog The catalog to be selected.
     */
    suspend fun selectCatalog(catalog: CatalogUiModel)
}

class MediaDetailsDelegate @Inject constructor(
    private val mediaUseCase: IMediaPersistenceUseCase,
    private val queryUseCase: ICatalogQueryStateUseCase,
) : IMediaDetailsDelegate {

    override suspend fun getIsLiked(id: Long): Boolean {
        return mediaUseCase.getById(id)?.isLiked ?: false
    }

    override suspend fun toggleLike(media: Media): Boolean {
        val newLikedStatus = !media.isLiked
        mediaUseCase.save(media.copy(isLiked = newLikedStatus))
        return newLikedStatus
    }

    override suspend fun selectCatalog(catalog: CatalogUiModel) {
        val currentQuery = queryUseCase.get() ?: QueryState()
        queryUseCase.save(currentQuery.copy(catalog = catalog.toDomain()))
    }

}
