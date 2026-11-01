package br.dev.singular.overview.presentation.model

import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodesProgressUiModelTest {

    @Test
    fun `percentage should round progress to the nearest integer`() {
        // arrange
        val progresses = listOf(0f, 0.081f, 0.085f, 1f)

        // act
        val percentages = progresses.map { inProgress(it).percentage }

        // assert
        assertEquals(listOf(0, 8, 9, 100), percentages)
    }

    @Test
    fun `percentage should be clamped between 0 and 100`() {
        // arrange
        val progresses = listOf(-0.5f, 1.5f)

        // act
        val percentages = progresses.map { inProgress(it).percentage }

        // assert
        assertEquals(listOf(0, 100), percentages)
    }

    private fun inProgress(progress: Float) = EpisodesProgressUiModel.InProgress(progress = progress)
}
