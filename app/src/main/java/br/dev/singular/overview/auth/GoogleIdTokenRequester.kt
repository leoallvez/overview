package br.dev.singular.overview.auth

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.Credential
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
     * Shows the Google account selector and waits for the user's choice.
     *
     * @param context The Activity context used to display the account selector.
     * @return [LoginIntent.Authenticate] with the ID token on success, [LoginIntent.Reset] when
     * the user dismisses the selector, or [LoginIntent.Fail] on any other outcome.
     */
    suspend fun request(context: Context): LoginIntent = try {
        credentialManager
            .getCredential(context, context.googleSignInRequest())
            .credential
            .toLoginIntent()
    } catch (e: GetCredentialCancellationException) {
        Timber.w(e)
        e.toLoginIntent()
    } catch (e: GetCredentialException) {
        failure(e)
    } catch (e: GoogleIdTokenParsingException) {
        failure(e)
    }

    private fun Context.googleSignInRequest(): GetCredentialRequest {
        val option = GetSignInWithGoogleOption
            .Builder(getString(R.string.default_web_client_id))
            .build()
        return GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()
    }

    private fun Credential.toLoginIntent(): LoginIntent {
        return if (isGoogleIdToken) {
            LoginIntent.Authenticate(GoogleIdTokenCredential.createFrom(data).idToken)
        } else {
            Timber.e("Unexpected credential type: $type")
            LoginIntent.Fail
        }
    }

    private val Credential.isGoogleIdToken: Boolean
        get() = this is CustomCredential && type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

    /**
     * Play services reports a missing OAuth registration (package + SHA-1) as a cancellation
     * with status 16, which must not be mistaken for the user dismissing the selector.
     */
    private fun GetCredentialCancellationException.toLoginIntent(): LoginIntent {
        val isConfigurationFailure = message.orEmpty().contains(RE_AUTH_FAILED_STATUS)
        return if (isConfigurationFailure) LoginIntent.Fail else LoginIntent.Reset
    }

    private fun failure(e: Exception): LoginIntent {
        Timber.e(e)
        return LoginIntent.Fail
    }

    private companion object {
        const val RE_AUTH_FAILED_STATUS = "[16]"
    }
}

/**
 * Remembers a callback that starts the Google sign in and delivers its outcome to [onResult].
 *
 * @param onResult Always invoked once per request, even if the composition is disposed first.
 * @return The callback that launches the sign in.
 */
@Composable
fun rememberGoogleIdTokenRequest(onResult: (LoginIntent) -> Unit): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentOnResult by rememberUpdatedState(onResult)
    val requester = remember(context) {
        GoogleIdTokenRequester(CredentialManager.create(context))
    }

    return remember(requester) {
        { scope.launch { requester.requestAlwaysDelivering(context) { currentOnResult(it) } } }
    }
}

/**
 * Delivers [LoginIntent.Reset] when the request is cancelled: the scope is cancelled when the
 * composition is disposed (e.g. on rotation) while the view model survives, so the loading
 * state must still be released.
 */
private suspend fun GoogleIdTokenRequester.requestAlwaysDelivering(
    context: Context,
    onResult: (LoginIntent) -> Unit
) {
    var result: LoginIntent = LoginIntent.Reset
    try {
        result = request(context)
    } finally {
        onResult(result)
    }
}
