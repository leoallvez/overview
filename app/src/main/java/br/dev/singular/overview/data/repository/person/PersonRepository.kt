package br.dev.singular.overview.data.repository.person

import br.dev.singular.overview.data.source.person.IPersonRemoteDataSource
import br.dev.singular.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val _dataSource: IPersonRemoteDataSource,
    @param:IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IPersonRepository {

    override suspend fun getItem(apiId: Long) = withContext(_dispatcher) {
        val result = _dataSource.getItem(apiId)
        flow { emit(result) }
    }
}
