package br.dev.singular.overview.ui.search

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagSearch
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.media.UiMediaList
import br.dev.singular.overview.ui.DefaultVerticalSpace
import br.dev.singular.overview.ui.IntermediateScreensText
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.NothingFoundScreen
import br.dev.singular.overview.ui.TagScreenView
import br.dev.singular.overview.ui.ToolbarTitle
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.getStringByName
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.tagging.TagMediaManager.Detail.SELECT_MEDIA_TYPE
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector

private fun tagClick(detail: String, id: Long = 0L) {
    TagManager.logClick(TagSearch.PATH, detail, id)
}

@Composable
fun SearchScreen(
    navigate: BasicNavigate,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val filters = viewModel.searchFilters.collectAsState().value
    val items = viewModel.mediasSearch.collectAsLazyPagingItems()
    val suggestionsUIState = viewModel.suggestionsUIState.collectAsState().value

    Scaffold(
        contentColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.spacing_small)),
        topBar = {
            SearchToolBar { query ->
                viewModel.onSearching(filters.copy(query = query))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(top = padding.calculateTopPadding())
        ) {
            if (items.itemCount > 0 || filters.query.isNotEmpty()) {
                UiMediaTypeSelector(type = MediaUiType.getByName(name = filters.mediaType.key)) {
                    val newType = MediaType.getByKey(it.name.lowercase())
                    tagClick("${SELECT_MEDIA_TYPE}${newType.key}")
                    viewModel.onSearching(filters.copy(mediaType = newType))
                }
            }
            DefaultVerticalSpace()
            Box {
                when (items.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen(TagSearch.PATH)
                    is LoadState.NotLoading -> {
                        TagScreenView(TagSearch.PATH, TagStatus.SUCCESS)
                        UiMediaGrid(
                            items = items,
                            onClick = {
                                TagMediaManager.logClick(TagSearch.PATH, it.id)
                                navigate.toMediaDetails(it)
                            }
                        )
                    }
                    else -> {
                        if (items.itemCount == 0 && filters.query.isNotEmpty()) {
                            NothingFoundScreen(TagSearch.PATH)
                        } else {
                            SearchInitialScreen(
                                suggestions = suggestionsUIState,
                                tagPath = TagSearch.PATH,
                                onClick = {
                                    TagMediaManager.logClick(TagSearch.PATH_SUGGESTIONS, it.id)
                                    navigate.toMediaDetails(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchToolBar(onSearch: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = dimensionResource(R.dimen.spacing_small))
    ) {
        ToolbarTitle(title = stringResource(id = R.string.search))
        DefaultVerticalSpace()
        SearchField(onSearch = onSearch)
    }
}

@Composable
fun SearchInitialScreen(
    suggestions: SuggestionUIState,
    tagPath: String,
    onClick: (MediaUiModel) -> Unit
) {
    when (suggestions) {
        is UiState.Loading -> LoadingScreen(tagPath)
        is UiState.Success -> {
            TagScreenView(tagPath, TagStatus.SUCCESS)
            SuggestionsVerticalList(suggestions = suggestions.data, onClick = onClick)
        }
        is UiState.Error -> {
            TagScreenView(tagPath, TagStatus.ERROR)
            CenteredTextString(R.string.search_not_started)
        }
    }
}

@Composable
fun CenteredTextString(@StringRes textRes: Int, color: Color = AccentColor) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IntermediateScreensText(stringResource(textRes), color)
        }
    }
}

@Composable
fun ClearSearchIcon(query: String, onClick: () -> Unit) {
    if (query.isNotEmpty()) {
        IconButton(onClick = onClick) {
            Icon(
                tint = AccentColor,
                imageVector = Icons.Rounded.Clear,
                contentDescription = stringResource(R.string.search_icon)
            )
        }
    }
}

@Composable
fun SearchIcon(modifier: Modifier = Modifier) {
    Icon(
        tint = AccentColor,
        modifier = modifier,
        imageVector = Icons.Rounded.Search,
        contentDescription = stringResource(R.string.search_icon)
    )
}

@Composable
fun SuggestionsVerticalList(
    suggestions: Map<String, List<MediaUiModel>>,
    onClick: (MediaUiModel) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        suggestions.forEach { (titleKey, mediaItems) ->
            UiMediaList(
                title = context.getStringByName(titleKey).orEmpty(),
                items = mediaItems,
                onClick = onClick
            )
        }
    }
}

@Composable
fun SearchField(onSearch: (String) -> Unit) {

    var query by rememberSaveable { mutableStateOf("") }
    Box(modifier = Modifier.background(PrimaryBackground)) {
        BasicTextField(
            value = query,
            enabled = true,
            modifier = Modifier.fillMaxWidth().height(40.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            onValueChange = { newValue ->
                TagManager.logInteraction(TagSearch.PATH, TagSearch.Detail.SEARCH_FIELD)
                onSearch.invoke(newValue.also { query = it })
            },
            keyboardOptions = KeyboardOptions(imeAction = Companion.Search),
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .border(
                            width = 1.dp,
                            color = if (query.isEmpty()) Gray.copy(alpha = 0.5f) else AccentColor,
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .padding(start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchIcon(
                        modifier = Modifier.padding(5.dp)
                    )
                    Box(Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                stringResource(R.string.search_in_all_places),
                                style = LocalTextStyle.current.copy(
                                    color = Gray,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                ),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        innerTextField()
                    }
                    if (query.isNotEmpty()) {
                        ClearSearchIcon(query) {
                            tagClick(TagSearch.Detail.CLEAN_SEARCH_FIELD)
                            query = ""
                        }
                    }
                }
            }
        )
    }
}
