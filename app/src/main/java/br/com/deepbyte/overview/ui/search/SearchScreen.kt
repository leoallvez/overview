package br.com.deepbyte.overview.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import br.com.deepbyte.overview.ui.SearchUiState
import br.com.deepbyte.overview.ui.ToolbarButton
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            SearchToolBar(onNavigateToHome) { query ->
                viewModel.search(query)
            }
        },
        bottomBar = {
            AdsBanner(R.string.banner_sample_id, viewModel.showAds)
        }
    ) { padding ->

        SearchBody(
            padding = padding,
            uiState = viewModel.uiState.collectAsState().value
        )
    }
}

@Composable
fun SearchToolBar(backButtonAction: () -> Unit, onSearch: (String) -> Unit) {

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

        SearchField(onSearch)
    }
}

@Composable
fun SearchField(onSearch: (String) -> Unit) {

    val query = rememberSaveable { mutableStateOf("") }
    val showClearIcon = rememberSaveable { mutableStateOf(false) }
    showClearIcon.value = query.value.isNotEmpty()

    OutlinedTextField(
        value = query.value,
        onValueChange = { value ->
            onSearch(value)
            query.value = value
        },
        maxLines = 1,
        placeholder = { Text(text = "Pesquisar", color = AccentColor) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        textStyle = MaterialTheme.typography.subtitle1.copy(color = Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = Color.White,
            backgroundColor = SecondaryBackground,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                tint = AccentColor,
                contentDescription = "Clear icon"
            )
        },
        trailingIcon = {
            if (showClearIcon.value) {
                IconButton(onClick = { query.value = "" }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        tint = AccentColor,
                        contentDescription = "Clear icon"
                    )
                }
            }
        },
        shape = RoundedCornerShape(50.dp)
    )
}

@Composable
fun SearchBody(
    uiState: SearchUiState,
    padding: PaddingValues
) {
    Text("screen body: $uiState", Modifier.padding(padding), color = Color.Black)
}
