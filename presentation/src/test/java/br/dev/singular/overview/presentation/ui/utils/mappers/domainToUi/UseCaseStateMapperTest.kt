package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.UiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UseCaseStateMapperTest {

    @Test
    fun `toUiState should map Success correctly`() {
        val success = UseCaseState.Success("Data")
        val result = success.toUiState { it.uppercase() }

        assertTrue(result is UiState.Success)
        assertTrue((result as UiState.Success).data == "DATA")
    }

    @Test
    fun `toUiState should map Failure correctly`() {
        val failure: UseCaseState<String> = UseCaseState.Failure(FailType.Invalid)
        val result = failure.toUiState { it.uppercase() }

        assertTrue(result is UiState.Error)
    }

    @Test
    fun `toUiStateNullable should map Success with null correctly`() {
        val success = UseCaseState.Success<String?>(null)
        val result = success.toUiStateNullable { it.uppercase() }

        assertTrue(result is UiState.Success)
        assertTrue((result as UiState.Success).data == null)
    }

    @Test
    fun `toActionState should map Success correctly`() {
        val success = UseCaseState.Success("Data")

        assertEquals(ActionState.Success, success.toActionState())
    }

    @Test
    fun `toActionState should map Failure correctly`() {
        val failure: UseCaseState<String> = UseCaseState.Failure(FailType.Invalid)

        assertEquals(ActionState.Error, failure.toActionState())
    }
}
