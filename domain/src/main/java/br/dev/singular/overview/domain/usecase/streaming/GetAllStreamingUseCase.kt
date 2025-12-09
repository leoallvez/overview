package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.GetAll

interface IGetAllStreamingUseCase  {
    suspend operator fun invoke(): List<Streaming>
}

class GetAllStreamingUseCase(
    private val getter: GetAll<Streaming>
) : IGetAllStreamingUseCase  {

    override suspend fun invoke() = runCatching {
            getter.getAll().filter { it.display }
        }.fold(
            onSuccess = { it },
            onFailure = { listOf() }
        )
}
