package br.dev.singular.overview.data.local.workers

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class CacheWorkSchedulerTest {

    private val workManager: WorkManager = mockk(relaxed = true)
    private val sut = CacheWorkScheduler(workManager)

    @Test
    fun `schedule should enqueue unique one-time workers and periodic worker`() {
        // arrange
        // act
        sut.schedule()

        // assert
        verify {
            workManager.enqueueUniqueWork(
                "delete_suggestions_cache_work",
                ExistingWorkPolicy.KEEP,
                any<OneTimeWorkRequest>()
            )
        }
        verify {
            workManager.enqueueUniqueWork(
                "delete_catalog_cache_work",
                ExistingWorkPolicy.KEEP,
                any<OneTimeWorkRequest>()
            )
        }
        verify {
            workManager.enqueueUniqueWork(
                "save_genre_cache_work",
                ExistingWorkPolicy.KEEP,
                any<OneTimeWorkRequest>()
            )
        }
        verify {
            workManager.enqueueUniquePeriodicWork(
                "refresh_catalog_cache_work",
                ExistingPeriodicWorkPolicy.UPDATE,
                any<PeriodicWorkRequest>()
            )
        }
    }

    @Test
    fun `schedule should require network connection for save genre cache worker`() {
        // arrange
        val request = slot<OneTimeWorkRequest>()

        // act
        sut.schedule()

        // assert
        verify {
            workManager.enqueueUniqueWork(
                "save_genre_cache_work",
                ExistingWorkPolicy.KEEP,
                capture(request)
            )
        }
        assertEquals(
            NetworkType.CONNECTED,
            request.captured.workSpec.constraints.requiredNetworkType
        )
    }
}
