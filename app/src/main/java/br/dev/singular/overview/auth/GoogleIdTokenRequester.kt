package br.dev.singular.overview.auth

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import br.dev.singular.overview.R
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginIntent
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Requests a Google ID token through the Credential Manager and translates the outcome into
 * the [LoginIntent] expected by the login flow.
 */
class GoogleIdTokenRequester(
    private val credentialManager: CredentialManager
) {

    /**
     * @param context The Activity context used to display the account selector.
     */
    suspend fun request(context: Context): LoginIntent {
        val option = GetSignInWithGoogleOption
            .Builder(context.getString(R.string.default_web_client_id))
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        return try {
            val credential = credentialManager.getCredential(context, request).credential
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                LoginIntent.Authenticate(idToken)
            } else {
                Timber.e("Unexpected credential type: ${credential.type}")
                LoginIntent.Fail
            }
        } catch (e: GetCredentialCancellationException) {
            Timber.w(e)
            if (e.isConfigurationFailure()) LoginIntent.Fail else LoginIntent.Reset
        } catch (e: GetCredentialException) {
            Timber.e(e)
            LoginIntent.Fail
        } catch (e: GoogleIdTokenParsingException) {
            Timber.e(e)
            LoginIntent.Fail
        }
    }

    // Play services reports a missing OAuth registration (package + SHA-1) as a cancellation
    // with status 16, which must not be mistaken for the user dismissing the selector.
    private fun GetCredentialCancellationException.isConfigurationFailure(): Boolean {
        return message.orEmpty().contains(REAUTH_FAILED_STATUS)
    }

    private companion object {
        const val REAUTH_FAILED_STATUS = "[16]"
    }
}

/**
 * Remembers a callback that starts the Google sign in and delivers its outcome to [onResult].
 */
@Composable
fun rememberGoogleIdTokenRequest(onResult: (LoginIntent) -> Unit): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentOnResult = rememberUpdatedState(onResult)
    val requester = remember(context) {
        GoogleIdTokenRequester(CredentialManager.create(context))
    }

    return remember(requester) {
        {
            scope.launch {
                // The scope is cancelled when the composition is disposed (e.g. on rotation)
                // while the view model survives, so the loading state must still be released.
                var result: LoginIntent = LoginIntent.Reset
                try {
                    result = requester.request(context)
                } finally {
                    currentOnResult.value(result)
                }
            }
            Unit
        }
    }
}
