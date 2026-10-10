package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.presentation.model.PredictionUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PredictionTest {

    @Test
    fun `Prediction toUi should map available prediction to predicted model`() {
        // arrange
        val domainModel = Prediction.Available(affinityPercentage = 75)

        // act
        val uiModel = domainModel.toUi()

        // assert
        assertEquals(PredictionUiModel.Predicted(affinityPercentage = 75), uiModel)
    }

    @Test
    fun `Prediction toUi should map unavailable prediction to unavailable model`() {
        // act
        val uiModel = Prediction.Unavailable.toUi()

        // assert
        assertEquals(PredictionUiModel.Unavailable, uiModel)
    }

    @Test
    fun `Prediction toUi should map error prediction to error model`() {
        // act
        val uiModel = Prediction.Error.toUi()

        // assert
        assertEquals(PredictionUiModel.Error, uiModel)
    }
}
