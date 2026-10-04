package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.ISyncFavoritesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SyncFavoritesWorkerTest {

    private val context: Context = mockk(relaxed = true)
    private val params: WorkerParameters = mockk(relaxed = true)
    private val useCase: ISyncFavoritesUseCase = mockk()

    @Test
    fun `doWork should report the changes when useCase changes the favorites`() = runTest {
        coEvery { useCase() } returns UseCaseState.Success(true)
        val sut = SyncFavoritesWorker(context, params, useCase)

        val result = sut.doWork()

        val output = workDataOf(SyncFavoritesWorker.HAS_CHANGES to true)
        assertEquals(ListenableWorker.Result.success(output), result)
    }

    @Test
    fun `doWork should report no changes when the favorites are already in sync`() = runTest {
        coEvery { useCase() } returns UseCaseState.Success(false)
        val sut = SyncFavoritesWorker(context, params, useCase)

        val result = sut.doWork()

        val output = workDataOf(SyncFavoritesWorker.HAS_CHANGES to false)
        assertEquals(ListenableWorker.Result.success(output), result)
    }

    @Test
    fun `doWork should return Failure when useCase returns Failure`() = runTest {
        coEvery { useCase() } returns UseCaseState.Failure(FailType.Invalid)
        val sut = SyncFavoritesWorker(context, params, useCase)

        val result = sut.doWork()

        assertEquals(ListenableWorker.Result.failure(), result)
    }
}
