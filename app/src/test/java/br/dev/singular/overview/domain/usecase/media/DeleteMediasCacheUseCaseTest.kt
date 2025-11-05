package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.DeleteUseCaseTest
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.mockk
import org.junit.Before
import java.util.Date

class DeleteMediasCacheUseCaseTest() : DeleteUseCaseTest<Media>() {

    override lateinit var getter: GetAll<Media>
    override lateinit var deleter: Delete<Media>

    private val today: Date = Date()

    @Before
    fun onSetup() {
        getter = mockk()
        deleter = mockk()
        sut = DeleteMediasUseCase(getter, deleter, maxCacheDate = today)
    }

    override fun createMockToKeep() = createOldMediaMock(isLiked = true)

    override fun createMockToDelete() = createOldMediaMock(isLiked = false)

    private fun createOldMediaMock(isLiked: Boolean): Media {
        val sevenDaysAgo = today.adjustDate(days = -7)
        return createMediaMock(isLiked = isLiked, lastUpdate = sevenDaysAgo)
    }
}
