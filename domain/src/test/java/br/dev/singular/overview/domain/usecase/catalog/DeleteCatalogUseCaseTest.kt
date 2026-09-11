package br.dev.singular.overview.domain.usecase.catalog

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.DeleteUseCaseTest
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.createCatalogMock
import io.mockk.mockk
import org.junit.Before
import java.util.Date

class DeleteCatalogUseCaseTest : DeleteUseCaseTest<Catalog>() {

    override lateinit var getter: GetAll<Catalog>
    override lateinit var deleter: Delete<Catalog>

    private val today: Date = Date()

    @Before
    fun onSetup() {
        getter = mockk()
        deleter = mockk()
        sut = DeleteCatalogUseCase(getter, deleter, maxCacheDate = today)
    }

    override fun createMockToKeep() =
        createCatalogMock(lastUpdate = today)

    override fun createMockToDelete() =
        createCatalogMock(lastUpdate = today.adjustDate(-1))

}
