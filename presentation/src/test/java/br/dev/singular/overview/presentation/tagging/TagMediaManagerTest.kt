package br.dev.singular.overview.presentation.tagging

import br.dev.singular.overview.presentation.model.MediaUiType
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TagMediaManagerTest {

    private val firebaseAnalytics: FirebaseAnalytics = mockk(relaxed = true)

    @Before
    fun setup() {
        TagManager.init(firebaseAnalytics)
    }

    @Test
    fun `logMediaClick should call TagManager logClick`() {
        TagMediaManager.logMediaClick("path", 1L)

        verify {
            firebaseAnalytics.logEvent("click", any())
        }
    }

    @Test
    fun `logTypeClick should call TagManager logClick with formatted detail`() {
        TagMediaManager.logTypeClick("path", MediaUiType.MOVIE)

        verify {
            firebaseAnalytics.logEvent("click", any())
        }
    }
}
