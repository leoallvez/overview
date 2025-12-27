package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Get

interface IGetSelectedStreamingUseCase {
    suspend operator fun invoke(): Streaming?
}

class GetSelectedStreamingUseCase(
    private val getter: Get<Streaming?>
) : IGetSelectedStreamingUseCase {

    override suspend fun invoke() = runCatching {
        getter.get()
    }.fold(
        onSuccess = { it },
        onFailure = { null }
    )
}
