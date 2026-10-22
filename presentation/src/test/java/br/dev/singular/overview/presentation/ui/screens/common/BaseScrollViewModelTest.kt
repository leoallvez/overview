package br.dev.singular.overview.presentation.ui.screens.common

import br.dev.singular.overview.presentation.model.ScrollUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class BaseScrollViewModelTest {

    @Test
    fun `onSetScrollState should update scrollState value`() {
        // arrange
        val sut = BaseScrollViewModel()
        val newState = ScrollUiState(index = 5, offset = 100)

        // act
        sut.onSetScrollState(newState)

        // assert
        assertEquals(newState, sut.scrollState.value)
    }

    @Test
    fun `initial scrollState should be default`() {
        // arrange
        val sut = BaseScrollViewModel()

        // assert
        assertEquals(ScrollUiState(), sut.scrollState.value)
    }
}
