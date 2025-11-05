package br.dev.singular.overview.domain.usecase.suggestion

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.DeleteUseCaseTest
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.createSuggestionMock
import io.mockk.mockk
import org.junit.Before
import java.util.Date

class DeleteSuggestionsUseCaseTest : DeleteUseCaseTest<Suggestion>()  {

    override lateinit var getter: GetAll<Suggestion>
    override lateinit var deleter: Delete<Suggestion>

    private val today: Date = Date()

    @Before
    fun onSetup() {
        getter = mockk()
        deleter = mockk()
        sut = DeleteSuggestionsUseCase(getter, deleter, maxCacheDate = today)
    }

    override fun createMockToKeep() =
        createSuggestionMock(lastUpdate = today)

    override fun createMockToDelete() =
        createSuggestionMock(lastUpdate = today.adjustDate(-1))
}
