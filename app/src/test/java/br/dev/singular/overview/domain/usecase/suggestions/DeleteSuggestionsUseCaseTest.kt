package br.dev.singular.overview.domain.usecase.suggestions

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.DeleteAll
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.suggetions.DeleteSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggetions.IDeleteSuggestionsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteSuggestionsUseCaseTest {

    private lateinit var sut: IDeleteSuggestionsUseCase
    private lateinit var deleteAllMock: DeleteAll<Suggestion>

    @Before
    fun setup() {
        deleteAllMock = mockk()
        sut = DeleteSuggestionsUseCase(deleteAllMock)
    }

    @Test
    fun `invoke should return success when delete all successfully`() = runTest {
        // arrange
        coEvery { deleteAllMock.deleteAll() } returns Unit
        // act
        val result = sut.invoke()
        // assert
        coVerify { deleteAllMock.deleteAll() }
        assertEquals(UseCaseState.Success(Unit), result)
    }

    @Test
    fun `invoke should return failure when delete all throws exception`() = runTest {
        // arrange
        coEvery { deleteAllMock.deleteAll() } throws Exception()
        // act
        val result = sut.invoke()
        // assert
        coVerify { deleteAllMock.deleteAll() }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}
