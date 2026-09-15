package br.dev.singular.overview.domain.usecase.user

import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Execute
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely
import kotlinx.coroutines.flow.Flow

interface IUserSessionUseCase {

    fun observe(): Flow<User?>
    suspend fun signIn(idToken: String): UseCaseState<User>
    suspend fun signOut(): UseCaseState<Unit>
}

class UserSessionUseCase(
    private val observer: Observe<User?>,
    private val authenticator: GetByParam<User, String>,
    private val executor: Execute
) : IUserSessionUseCase {

    override fun observe(): Flow<User?> = observer.observe()

    override suspend fun signIn(idToken: String): UseCaseState<User> {
        if (idToken.isBlank()) return UseCaseState.Failure(FailType.Invalid)
        return runSafely { authenticator.getByParam(idToken) }
    }

    override suspend fun signOut() = runSafely { executor.execute() }
}
