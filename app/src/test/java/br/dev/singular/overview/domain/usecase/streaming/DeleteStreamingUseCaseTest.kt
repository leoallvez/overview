package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.DeleteUseCaseTest
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.createStreamingMock
import io.mockk.mockk
import org.junit.Before
import java.util.Date

class DeleteStreamingUseCaseTest() : DeleteUseCaseTest<Streaming>() {

    override lateinit var getter: GetAll<Streaming>
    override lateinit var deleter: Delete<Streaming>

    private val today: Date = Date()

    @Before
    fun onSetup() {
        getter = mockk()
        deleter = mockk()
        sut = DeleteStreamingUseCase(getter, deleter, maxCacheDate = today)
    }

    override fun createMockToKeep() =
        createStreamingMock(lastUpdate = today)

    override fun createMockToDelete() =
        createStreamingMock(lastUpdate = today.adjustDate(-1))

}
