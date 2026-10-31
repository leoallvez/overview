package br.dev.singular.overview.presentation.ui.components.progress

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiProgressBarTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should expose progress as progress bar range info`() {
        // act
        rule.setContent {
            UiProgressBar(progress = 0.5f)
        }

        // assert
        rule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo(0.5f, 0f..1f))).assertExists()
    }

    @Test
    fun `should clamp progress above 1`() {
        // act
        rule.setContent {
            UiProgressBar(progress = 1.5f)
        }

        // assert
        rule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo(1f, 0f..1f))).assertExists()
    }

    @Test
    fun `should clamp progress below 0`() {
        // act
        rule.setContent {
            UiProgressBar(progress = -0.5f)
        }

        // assert
        rule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo(0f, 0f..1f))).assertExists()
    }
}
