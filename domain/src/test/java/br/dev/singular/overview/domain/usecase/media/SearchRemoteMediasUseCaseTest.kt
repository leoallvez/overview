package br.dev.singular.overview.domain.usecase.media

import io.mockk.mockk
import org.junit.Before

class SearchRemoteMediasUseCaseTest : GetRemoteMediasUseCaseTest(keyPrefix = "search") {

    @Before
    fun setup() {
        getter = mockk()
        sut = SearchRemoteMediasUseCase(getter)
    }
}
