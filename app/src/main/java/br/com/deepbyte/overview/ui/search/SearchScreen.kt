package br.com.deepbyte.overview.ui.search

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    viewModel.search(query = "A")
    val uiState = viewModel.uiState.collectAsState().value
    Text("search screen open $uiState", color = Color.Black)
}
