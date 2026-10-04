package br.dev.singular.overview.data.network.source

import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialUnknownException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AuthRemoteDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var auth: FirebaseAuth

    @MockK(relaxed = true)
    private lateinit var credentialManager: CredentialManager

    private val credential: AuthCredential = mockk()

    private lateinit var sut: IAuthRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(GoogleAuthProvider::class)
        every { GoogleAuthProvider.getCredential(any(), any()) } returns credential
        sut = AuthRemoteDataSource(auth, credentialManager)
    }

    @After
    fun tearDown() {
        unmockkStatic(GoogleAuthProvider::class)
    }

    @Test
    fun `observe should emit the current user and remove the listener when cancelled`() = runTest {
        // arrange
        val user = createUser()
        every { auth.currentUser } returns user
        every { auth.addAuthStateListener(any()) } answers {
            firstArg<FirebaseAuth.AuthStateListener>().onAuthStateChanged(auth)
        }

        // act
        val result = sut.observe().first()

        // assert
        assertEquals("uid", result?.id)
        assertEquals("Celeste Beaumont", result?.name)
        assertEquals("celeste@example.com", result?.email)
        assertNull(result?.photoUrl)
        verify(exactly = 1) { auth.removeAuthStateListener(any()) }
    }

    @Test
    fun `observe should emit null when there is no session`() = runTest {
        // arrange
        every { auth.currentUser } returns null
        every { auth.addAuthStateListener(any()) } answers {
            firstArg<FirebaseAuth.AuthStateListener>().onAuthStateChanged(auth)
        }

        // act
        val result = sut.observe().first()

        // assert
        assertNull(result)
    }

    @Test
    fun `currentUser should return the signed in user`() {
        // arrange
        every { auth.currentUser } returns createUser()

        // act
        val result = sut.currentUser()

        // assert
        assertEquals("uid", result?.id)
    }

    @Test
    fun `currentUser should return null when there is no session`() {
        // arrange
        every { auth.currentUser } returns null

        // act & assert
        assertNull(sut.currentUser())
    }

    @Test
    fun `signIn should return the authenticated user`() = runTest {
        // arrange
        val user = createUser()
        every { auth.signInWithCredential(credential) } returns createTask(user)

        // act
        val result = sut.signIn("token")

        // assert
        assertEquals("uid", result.id)
        verify(exactly = 1) { GoogleAuthProvider.getCredential("token", null) }
    }

    @Test
    fun `signIn should throw when the authenticated user is missing`() {
        // arrange
        every { auth.signInWithCredential(credential) } returns createTask(user = null)

        // act & assert
        assertThrows(IllegalStateException::class.java) {
            runTest { sut.signIn("token") }
        }
    }

    @Test
    fun `signIn should throw when the task fails`() {
        // arrange
        val task = mockk<Task<AuthResult>>()
        every { task.isComplete } returns true
        every { task.exception } returns IllegalArgumentException("invalid token")
        every { auth.signInWithCredential(credential) } returns task

        // act & assert
        assertThrows(IllegalArgumentException::class.java) {
            runTest { sut.signIn("token") }
        }
    }

    @Test
    fun `signOut should clear the credential state before signing out`() = runTest {
        // act
        sut.signOut()

        // assert
        coVerifyOrder {
            credentialManager.clearCredentialState(any())
            auth.signOut()
        }
    }

    @Test
    fun `signOut should sign out even when clearing the credential state fails`() = runTest {
        // arrange
        coEvery {
            credentialManager.clearCredentialState(any())
        } throws ClearCredentialUnknownException()

        // act
        sut.signOut()

        // assert
        verify(exactly = 1) { auth.signOut() }
    }

    private fun createUser(): FirebaseUser = mockk {
        every { uid } returns "uid"
        every { displayName } returns "Celeste Beaumont"
        every { email } returns "celeste@example.com"
        every { photoUrl } returns null
    }

    private fun createTask(user: FirebaseUser?): Task<AuthResult> {
        val authResult = mockk<AuthResult>()
        every { authResult.user } returns user
        return mockk {
            every { isComplete } returns true
            every { isCanceled } returns false
            every { exception } returns null
            every { result } returns authResult
        }
    }
}
