package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Update

interface ICatalogTooltipDismissedUseCase {
    suspend fun isDismissed(): Boolean
    suspend fun dismiss()
}

class CatalogTooltipDismissedUseCase(
    private val getter: Get<Boolean>,
    private val updater: Update<Boolean>
) : ICatalogTooltipDismissedUseCase {

    override suspend fun isDismissed(): Boolean = getter.get()

    override suspend fun dismiss() = updater.update(true)
}
