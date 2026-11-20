package br.dev.singular.overview.domain.usecase.media

import io.mockk.mockk
import org.junit.Before

class DiscoverRemoteMediasUseCaseTest : GetRemoteMediasUseCaseTest(keyPrefix = "discover") {

    @Before
    fun setup() {
        getter = mockk()
        sut = DiscoverRemoteMediasUseCase(getter)
    }
}
