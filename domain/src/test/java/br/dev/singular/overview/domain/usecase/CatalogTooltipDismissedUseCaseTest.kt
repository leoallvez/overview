package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Update
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CatalogTooltipDismissedUseCaseTest {

    private lateinit var sut: ICatalogTooltipDismissedUseCase
    private val getter: Get<Boolean> = mockk()
    private val updater: Update<Boolean> = mockk()

    @Before
    fun setup() {
        sut = CatalogTooltipDismissedUseCase(getter, updater)
    }

    @After
    fun tearDown() {
        confirmVerified(getter, updater)
    }

    @Test
    fun `isDismissed should return true when data source returns true`() = runTest {
        // arrange
        coEvery { getter.get() } returns true

        // act
        val result = sut.isDismissed()

        // assert
        assertEquals(true, result)
        coVerify(exactly = 1) { getter.get() }
    }

    @Test
    fun `isDismissed should return false when data source returns false`() = runTest {
        // arrange
        coEvery { getter.get() } returns false

        // act
        val result = sut.isDismissed()

        // assert
        assertEquals(false, result)
        coVerify(exactly = 1) { getter.get() }
    }

    @Test
    fun `isDismissed should propagate exception when getter fails`() = runTest {
        // arrange
        val expectedException = RuntimeException("DataStore error")
        coEvery { getter.get() } throws expectedException

        // act & assert
        try {
            sut.isDismissed()
        } catch (actual: RuntimeException) {
            assertEquals(expectedException.message, actual.message)
        }
        coVerify(exactly = 1) { getter.get() }
    }

    @Test
    fun `dismiss should update state to true`() = runTest {
        // arrange
        coEvery { updater.update(true) } returns Unit

        // act
        sut.dismiss()

        // assert
        coVerify(exactly = 1) { updater.update(true) }
    }

    @Test
    fun `dismiss should propagate exception when updater fails`() = runTest {
        // arrange
        val expectedException = RuntimeException("Update error")
        coEvery { updater.update(true) } throws expectedException

        // act & assert
        try {
            sut.dismiss()
        } catch (actual: RuntimeException) {
            assertEquals(expectedException.message, actual.message)
        }
        coVerify(exactly = 1) { updater.update(true) }
    }
}
