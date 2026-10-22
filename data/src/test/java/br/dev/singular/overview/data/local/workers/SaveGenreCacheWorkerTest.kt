package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.genre.ISaveGenreUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveGenreCacheWorkerTest {

    private val context: Context = mockk(relaxed = true)
    private val params: WorkerParameters = mockk(relaxed = true)
    private val useCase: ISaveGenreUseCase = mockk()

    @Test
    fun `doWork should return Success when useCase returns Success`() = runTest {
        coEvery { useCase() } returns UseCaseState.Success(Unit)
        val sut = SaveGenreCacheWorker(context, params, useCase)

        val result = sut.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `doWork should return Failure when useCase returns Failure`() = runTest {
        coEvery { useCase() } returns UseCaseState.Failure(FailType.Invalid)
        val sut = SaveGenreCacheWorker(context, params, useCase)

        val result = sut.doWork()

        assertEquals(ListenableWorker.Result.failure(), result)
    }
}
