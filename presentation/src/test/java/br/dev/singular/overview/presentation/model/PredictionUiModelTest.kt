package br.dev.singular.overview.presentation.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PredictionUiModelTest {

    @Test
    fun `level should be HIGH when percentage is at or above the high threshold`() {
        // arrange
        val percentages = listOf(80, 100)

        // act
        val levels = percentages.map { PredictionUiModel.Predicted(it).level }

        // assert
        assertEquals(listOf(AffinityLevel.HIGH, AffinityLevel.HIGH), levels)
    }

    @Test
    fun `level should be MEDIUM when percentage is between the thresholds`() {
        // arrange
        val percentages = listOf(50, 79)

        // act
        val levels = percentages.map { PredictionUiModel.Predicted(it).level }

        // assert
        assertEquals(listOf(AffinityLevel.MEDIUM, AffinityLevel.MEDIUM), levels)
    }

    @Test
    fun `level should be LOW when percentage is below the medium threshold`() {
        // arrange
        val percentages = listOf(0, 49)

        // act
        val levels = percentages.map { PredictionUiModel.Predicted(it).level }

        // assert
        assertEquals(listOf(AffinityLevel.LOW, AffinityLevel.LOW), levels)
    }

    @Test
    fun `progress should be the percentage as a fraction`() {
        // arrange
        val model = PredictionUiModel.Predicted(affinityPercentage = 25)

        // act
        val progress = model.progress

        // assert
        assertEquals(0.25f, progress)
    }

    @Test
    fun `should reject percentage out of range`() {
        // arrange
        val percentages = listOf(-1, 101)

        // act
        val creations = percentages.map { percentage ->
            { PredictionUiModel.Predicted(affinityPercentage = percentage) }
        }

        // assert
        creations.forEach { create ->
            assertThrows(IllegalArgumentException::class.java) { create() }
        }
    }
}
