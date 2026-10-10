package br.dev.singular.overview.presentation.ui.components.prediction

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.PredictionUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiAiPredictionCardTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should display idle state with action button when model is Idle`() {
        // arrange
        val model = PredictionUiModel.Idle

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_title)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_idle)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_action)).assertIsDisplayed()
        rule.onNodeWithText("%", substring = true).assertDoesNotExist()
    }

    @Test
    fun `should trigger onPredict without try again when action button is clicked`() {
        // arrange
        val clicks = mutableListOf<Boolean>()
        rule.setContent {
            UiAiPredictionCard(
                model = PredictionUiModel.Idle,
                onPredict = { clicks += it }
            )
        }

        // act
        rule.onNodeWithText(string(R.string.taste_prediction_action)).performClick()

        // assert
        assertEquals(listOf(false), clicks)
    }

    @Test
    fun `should not display action button when result is predicted`() {
        // arrange
        val model = PredictionUiModel.Predicted(affinityPercentage = 92)

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_action)).assertDoesNotExist()
    }

    @Test
    fun `should display high affinity state when percentage is high`() {
        // arrange
        val model = PredictionUiModel.Predicted(affinityPercentage = 92)

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText("92%").assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_title)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_high)).assertIsDisplayed()
    }

    @Test
    fun `should display medium affinity state when percentage is medium`() {
        // arrange
        val model = PredictionUiModel.Predicted(affinityPercentage = 50)

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText("50%").assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_medium)).assertIsDisplayed()
    }

    @Test
    fun `should display low affinity state when percentage is low`() {
        // arrange
        val model = PredictionUiModel.Predicted(affinityPercentage = 10)

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText("10%").assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_low)).assertIsDisplayed()
    }

    @Test
    fun `should display unavailable state when model is Unavailable`() {
        // arrange
        val model = PredictionUiModel.Unavailable

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_no_data)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_unavailable)).assertIsDisplayed()
    }

    @Test
    fun `should display login required state with login button when model is LoginRequired`() {
        // arrange
        val model = PredictionUiModel.LoginRequired

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText(string(R.string.login_required_title)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_login_description)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.login)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_action)).assertDoesNotExist()
    }

    @Test
    fun `should trigger onLogin when login button is clicked`() {
        // arrange
        var clicks = 0
        val predictions = mutableListOf<Boolean>()
        rule.setContent {
            UiAiPredictionCard(
                model = PredictionUiModel.LoginRequired,
                onPredict = { predictions += it },
                onLogin = { clicks++ }
            )
        }

        // act
        rule.onNodeWithText(string(R.string.login)).performClick()

        // assert
        assertEquals(1, clicks)
        assertEquals(emptyList<Boolean>(), predictions)
    }

    @Test
    fun `should not display login button when model is Unavailable`() {
        // arrange
        val model = PredictionUiModel.Unavailable

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText(string(R.string.login)).assertDoesNotExist()
    }

    @Test
    fun `should display error state and try again button when model is Error`() {
        // arrange
        val model = PredictionUiModel.Error

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_error_title)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_error_description)).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.btn_try_again)).assertIsDisplayed()
    }

    @Test
    fun `should trigger onPredict as try again when try again button is clicked in error state`() {
        // arrange
        val clicks = mutableListOf<Boolean>()
        rule.setContent {
            UiAiPredictionCard(
                model = PredictionUiModel.Error,
                onPredict = { clicks += it }
            )
        }

        // act
        rule.onNodeWithText(string(R.string.btn_try_again)).performClick()

        // assert
        assertEquals(listOf(true), clicks)
    }

    @Test
    fun `should render skeleton when model is Loading`() {
        // arrange
        val model = PredictionUiModel.Loading

        // act
        rule.setContent {
            UiAiPredictionCard(model = model)
        }

        // assert
        rule.onNodeWithText(string(R.string.taste_prediction_title)).assertDoesNotExist()
    }

    @Test
    fun `skeleton should render without content text`() {
        // arrange
        val tag = "prediction_card_skeleton"

        // act
        rule.setContent {
            UiAiPredictionCardSkeleton(
                modifier = Modifier.testTag(tag)
            )
        }

        // assert
        rule.onNodeWithTag(tag).assertIsDisplayed()
        rule.onNodeWithText(string(R.string.taste_prediction_title)).assertDoesNotExist()
    }

    private fun string(@StringRes id: Int) = RuntimeEnvironment.getApplication().getString(id)
}
