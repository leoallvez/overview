package br.com.deepbyte.overview.data.repository

import br.com.deepbyte.overview.data.source.person.IPersonRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val _remoteDataSource: IPersonRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) {

    suspend fun getPersonDetails(apiId: Long) = withContext(_dispatcher) {
        return@withContext flow {
            emit(_remoteDataSource.getPersonDetails(apiId))
        }
    }
}
