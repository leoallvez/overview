package br.dev.singular.overview.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class UiStateTest {

    @Test
    fun `mapSuccess should transform the data of a Success state`() {
        // arrange
        val state: UiState<Int> = UiState.Success(1)

        // act
        val result = state.mapSuccess { it + 1 }

        // assert
        assertEquals(UiState.Success(2), result)
    }

    @Test
    fun `mapSuccess should keep a Loading state unchanged`() {
        // arrange
        val state: UiState<Int> = UiState.Loading()

        // act
        val result = state.mapSuccess { it + 1 }

        // assert
        assertSame(state, result)
    }

    @Test
    fun `mapSuccess should keep an Error state unchanged`() {
        // arrange
        val state: UiState<Int> = UiState.Error()

        // act
        val result = state.mapSuccess { it + 1 }

        // assert
        assertSame(state, result)
    }
}
