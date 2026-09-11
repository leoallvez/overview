package br.dev.singular.overview.presentation.ui.screens.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface UiEvent {
    data object ReloadFavorites : UiEvent
}

@Singleton
class UiEvents @Inject constructor() {

    private val _stream = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 16
    )

    val stream = _stream.asSharedFlow()

    fun trigger(event: UiEvent) {
        _stream.tryEmit(event)
    }
}
