package br.com.deepbyte.overview.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.ui.AdsBanner
import br.com.deepbyte.overview.ui.ToolbarButton
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    viewModel.search(query = "A")
    // val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = { SearchToolBar(onNavigateToHome) },
        bottomBar = {
            AdsBanner(R.string.banner_sample_id, viewModel.showAds)
        }
    ) { padding ->

        SearchBody(padding)
    }
}

@Composable
fun SearchToolBar(backButtonAction: () -> Unit) {

    val query = rememberSaveable { mutableStateOf("") }
    val showClearIcon = rememberSaveable { mutableStateOf(false) }

    showClearIcon.value = query.value.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolbarButton(
            painter = Icons.Default.KeyboardArrowLeft,
            descriptionResource = R.string.back_to_home_icon,
            background = Color.White.copy(alpha = 0.1f),
            padding = PaddingValues(
                vertical = dimensionResource(R.dimen.screen_padding),
                horizontal = 2.dp
            )
        ) { backButtonAction.invoke() }

        Spacer(Modifier.size(12.dp))

        OutlinedTextField(
            value = query.value,
            onValueChange = { query.value = it },
            maxLines = 1,
            placeholder = { Text(text = "Pesquisar", color = AccentColor) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = MaterialTheme.typography.subtitle1.copy(color = AccentColor),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = AccentColor, unfocusedBorderColor = Color.White
            ),
            trailingIcon = {
                if (showClearIcon.value) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            tint = AccentColor,
                            contentDescription = "Clear icon"
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        tint = AccentColor,
                        contentDescription = "Clear icon"
                    )
                }
            }
        )
    }
}

@Composable
fun SearchBody(padding: PaddingValues) {
    Text("screen body", Modifier.padding(padding))
}
