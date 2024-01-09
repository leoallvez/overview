package br.dev.singular.overview.ui.liked

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate

@Composable
fun LikedScreen(
    navigate: BasicNavigate,
    viewModel: LikedViewModel = hiltViewModel()
) {
    Text("LikedScreen")
}
