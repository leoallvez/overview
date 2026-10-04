package br.dev.singular.overview.domain.usecase.user

import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Clear
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createUserMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserSessionUseCaseTest {

    private lateinit var sut: IUserSessionUseCase
    private val observer: Observe<User?> = mockk()
    private val authenticator: GetByParam<User, String> = mockk()
    private val cleaner: Clear = mockk()

    @Before
    fun setup() {
        sut = UserSessionUseCase(observer, authenticator, cleaner)
    }

    @After
    fun tearDown() {
        confirmVerified(observer, authenticator, cleaner)
    }

    @Test
    fun `observe should return flow from repository`() = runTest {
        // arrange
        val expected = createUserMock()
        every { observer.observe() } returns flowOf(expected)

        // act
        val result = sut.observe().first()

        // assert
        assertEquals(expected, result)
        verify(exactly = 1) { observer.observe() }
    }

    @Test
    fun `signIn should return Success when repository authenticates`() = runTest {
        // arrange
        val expected = createUserMock()
        coEvery { authenticator.getByParam("token") } returns expected

        // act
        val result = sut.signIn("token")

        // assert
        assertEquals(UseCaseState.Success(expected), result)
        coVerify(exactly = 1) { authenticator.getByParam("token") }
    }

    @Test
    fun `signIn should return Invalid failure when token is blank`() = runTest {
        // act
        val result = sut.signIn(" ")

        // assert
        assertEquals(UseCaseState.Failure(FailType.Invalid), result)
    }

    @Test
    fun `signIn should return Exception failure when repository fails`() = runTest {
        // arrange
        val exception = RuntimeException("auth error")
        coEvery { authenticator.getByParam(any()) } throws exception

        // act
        val result = sut.signIn("token")

        // assert
        assertEquals(UseCaseState.Failure(FailType.Exception(exception)), result)
        coVerify(exactly = 1) { authenticator.getByParam("token") }
    }

    @Test
    fun `signOut should return Success when repository clears the session`() = runTest {
        // arrange
        coEvery { cleaner.clear() } returns Unit

        // act
        val result = sut.signOut()

        // assert
        assertTrue(result is UseCaseState.Success)
        coVerify(exactly = 1) { cleaner.clear() }
    }

    @Test
    fun `signOut should return Exception failure when repository fails`() = runTest {
        // arrange
        val exception = RuntimeException("sign out error")
        coEvery { cleaner.clear() } throws exception

        // act
        val result = sut.signOut()

        // assert
        assertEquals(UseCaseState.Failure(FailType.Exception(exception)), result)
        coVerify(exactly = 1) { cleaner.clear() }
    }
}
