package br.dev.singular.overview.ui

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.provider.StreamingData
import br.dev.singular.overview.presentation.UiState

typealias MediaUiState = UiState<Media?>
typealias StreamingUiState = UiState<StreamingData>
