package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UseCaseWorkerTest {

    private val context: Context = mockk(relaxed = true)
    private val params: WorkerParameters = mockk(relaxed = true)

    @Test
    fun `doWork should return Success when runWork returns Success`() = runTest {
        val sut = object : UseCaseWorker(context, params) {
            override suspend fun runWork() = UseCaseState.Success(Unit)
        }

        val result = sut.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `doWork should return Failure when runWork returns Failure`() = runTest {
        val sut = object : UseCaseWorker(context, params) {
            override suspend fun runWork() = UseCaseState.Failure(FailType.Invalid)
        }

        val result = sut.doWork()

        assertEquals(ListenableWorker.Result.failure(), result)
    }
}
