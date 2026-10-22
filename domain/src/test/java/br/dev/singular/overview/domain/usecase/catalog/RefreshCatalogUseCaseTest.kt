package br.dev.singular.overview.domain.usecase.catalog

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.createCatalogMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

class RefreshCatalogUseCaseTest {

    private lateinit var sut: IRefreshCatalogUseCase
    private lateinit var localGetter: GetAll<Catalog>
    private lateinit var remoteGetter: GetAll<Catalog>
    private lateinit var updater: Update<List<Catalog>>

    private val today = Date()
    private val yesterday = today.adjustDate(days = -1)

    @Before
    fun setup() {
        localGetter = mockk()
        remoteGetter = mockk()
        updater = mockk()
        coEvery { updater.update(any()) } returns Unit
        sut = RefreshCatalogUseCase(localGetter, remoteGetter, updater, currentDate = { today })
    }

    @Test
    fun `invoke should update cached catalogs with remote info and return success`() = runTest {
        // arrange
        val cached = createCatalogMock(lastUpdate = yesterday).copy(priority = 5, display = false)
        val remote = cached.copy(name = "Netflix Basic", logoPath = "new/logo", priority = 1)
        coEvery { localGetter.getAll() } returns listOf(cached)
        coEvery { remoteGetter.getAll() } returns listOf(remote)

        // act
        val result = sut.invoke()

        // assert
        assertEquals(UseCaseState.Success(Unit), result)
        val expected = cached.copy(name = "Netflix Basic", logoPath = "new/logo", lastUpdate = today)
        coVerify(exactly = 1) { updater.update(listOf(expected)) }
    }

    @Test
    fun `invoke should ignore remote catalogs that are not cached`() = runTest {
        // arrange
        val cached = createCatalogMock(lastUpdate = yesterday)
        val notCached = createCatalogMock().copy(id = 99L, name = "Other")
        coEvery { localGetter.getAll() } returns listOf(cached)
        coEvery { remoteGetter.getAll() } returns listOf(cached, notCached)

        // act
        sut.invoke()

        // assert
        coVerify(exactly = 1) { updater.update(listOf(cached.copy(lastUpdate = today))) }
    }

    @Test
    fun `invoke should update cache with empty list when no cached catalog is found remotely`() =
        runTest {
            // arrange
            coEvery { localGetter.getAll() } returns listOf(createCatalogMock())
            coEvery { remoteGetter.getAll() } returns emptyList()

            // act
            val result = sut.invoke()

            // assert
            assertEquals(UseCaseState.Success(Unit), result)
            coVerify(exactly = 1) { updater.update(emptyList()) }
        }

    @Test
    fun `invoke should return failure when remote getter throws exception`() = runTest {
        // arrange
        val expectedException = RuntimeException("Remote getter failed")
        coEvery { remoteGetter.getAll() } throws expectedException

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(expectedException, (failure.type as FailType.Exception).throwable)
    }

    @Test
    fun `invoke should return failure when updater throws exception`() = runTest {
        // arrange
        val expectedException = RuntimeException("Updater failed")
        val cached = createCatalogMock()
        coEvery { localGetter.getAll() } returns listOf(cached)
        coEvery { remoteGetter.getAll() } returns listOf(cached)
        coEvery { updater.update(any()) } throws expectedException

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(expectedException, (failure.type as FailType.Exception).throwable)
    }
}
