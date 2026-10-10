package br.dev.singular.overview.data.repository.user

import br.dev.singular.overview.data.local.source.IPredictionLocalDataSource
import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Execute
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Observe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dataSource: IAuthRemoteDataSource,
    private val predictionDataSource: IPredictionLocalDataSource
) : GetByParam<User, String>, Get<User?>, Observe<User?>, Execute {

    override fun observe(): Flow<User?> = dataSource.observe().map { it?.toDomain() }

    override suspend fun get(): User? = dataSource.currentUser()?.toDomain()

    override suspend fun getByParam(param: String): User = dataSource.signIn(param).toDomain()

    override suspend fun execute() {
        dataSource.signOut()
        predictionDataSource.clear()
    }
}
