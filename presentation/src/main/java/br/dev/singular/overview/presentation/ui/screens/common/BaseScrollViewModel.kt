package br.dev.singular.overview.presentation.ui.screens.common

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.presentation.model.ScrollUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseScrollViewModel : ViewModel() {

    private val _scrollState = MutableStateFlow(ScrollUiState())
    val scrollState: StateFlow<ScrollUiState> = _scrollState

    fun onSetScrollState(state: ScrollUiState) {
        _scrollState.value = state
    }
}
