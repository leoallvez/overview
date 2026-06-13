package br.dev.singular.overview.presentation.tagging

import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TagManagerTest {

    private val firebaseAnalytics: FirebaseAnalytics = mockk(relaxed = true)

    @Before
    fun setup() {
        TagManager.init(firebaseAnalytics)
    }

    @Test
    fun `logScreenView should call firebase logEvent correctly`() {
        val path = "test/path"
        val status = "success"

        TagManager.logScreenView(path, status)

        verify {
            firebaseAnalytics.logEvent("screen_view", any())
        }
    }

    @Test
    fun `logClick should call firebase logEvent correctly`() {
        val path = "test/path"
        val detail = "button-click"
        val id = 123L

        TagManager.logClick(path, detail, id)

        verify {
            firebaseAnalytics.logEvent("click", any())
        }
    }

    @Test
    fun `logInteraction should call firebase logEvent correctly`() {
        val path = "test/path"
        val detail = "scroll"

        TagManager.logInteraction(path, detail)

        verify {
            firebaseAnalytics.logEvent("interaction", any())
        }
    }
}
