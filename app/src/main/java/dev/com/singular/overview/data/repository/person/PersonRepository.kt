package dev.com.singular.overview.data.repository.person

import dev.com.singular.overview.data.source.person.IPersonRemoteDataSource
import dev.com.singular.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val _remoteDataSource: IPersonRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IPersonRepository {

    override suspend fun getItem(apiId: Long) = withContext(_dispatcher) {
        val result = _remoteDataSource.getItem(apiId)
        flow { emit(result) }
    }
}
