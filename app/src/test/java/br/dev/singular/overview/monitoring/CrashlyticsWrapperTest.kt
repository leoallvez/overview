package br.dev.singular.overview.monitoring

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class CrashlyticsWrapperTest {

    private val firebaseCrashlytics: FirebaseCrashlytics = mockk(relaxed = true)
    private val sut = CrashlyticsWrapper(firebaseCrashlytics)

    @Test
    fun `recordException should call firebase`() {
        // arrange
        val throwable = Throwable("test")

        // act
        sut.recordException(throwable)

        // assert
        verify { firebaseCrashlytics.recordException(throwable) }
    }

    @Test
    fun `log should call firebase`() {
        // arrange
        val message = "test message"

        // act
        sut.log(message)

        // assert
        verify { firebaseCrashlytics.log(message) }
    }
}
