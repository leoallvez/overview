package br.dev.singular.overview.data.network.source

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import br.dev.singular.overview.data.model.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface IAuthRemoteDataSource {
    fun observe(): Flow<UserDataModel?>
    fun currentUser(): UserDataModel?
    suspend fun signIn(idToken: String): UserDataModel
    suspend fun signOut()
}

class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager
) : IAuthRemoteDataSource {

    override fun observe(): Flow<UserDataModel?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser?.toData()) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun currentUser(): UserDataModel? = auth.currentUser?.toData()

    override suspend fun signIn(idToken: String): UserDataModel {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val user = auth.signInWithCredential(credential).await().user
        return checkNotNull(user) { "Authenticated user not found" }.toData()
    }

    override suspend fun signOut() {
        // The credential state is cleared first because observers react to the Firebase sign out.
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: ClearCredentialException) {
            Timber.w(e)
        }
        auth.signOut()
    }

    private fun FirebaseUser.toData() = UserDataModel(
        id = uid,
        name = displayName,
        email = email,
        photoUrl = photoUrl?.toString()
    )
}
