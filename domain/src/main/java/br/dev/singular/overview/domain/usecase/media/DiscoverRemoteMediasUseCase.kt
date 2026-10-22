package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage

class DiscoverRemoteMediasUseCase(
    private val getter: GetPage<Media, QueryState>
) : IGetRemoteMediasUseCase by BaseGetRemoteMediasUseCase(
    getter = getter,
    keyPrefix = "discover"
)
