package br.dev.singular.overview.ui.streaming.select

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.R
import br.dev.singular.overview.data.model.provider.StreamingData
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.ui.AdsBanner
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SimpleTitle
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.onClick
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SelectStreamingScreen(
    navigate: BasicNavigate,
    viewModel: SelectStreamingViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.SelectStreaming, tracker = viewModel.analyticsTracker)
    Scaffold(
        modifier = Modifier.padding(
            horizontal = dimensionResource(R.dimen.screen_padding_new)
        ),
        topBar = {
            ToolBar(onBackstack = navigate::popBackStack)
        },
        bottomBar = {
            AdsBanner(R.string.select_streaming_banner, isVisible = viewModel.showAds)
        },
        contentColor = PrimaryBackground
    ) { padding ->
        UiStateResult(
            uiState = viewModel.uiState.collectAsState().value,
            onRefresh = { viewModel.refresh() }
        ) { streamingData ->
            Column(modifier = Modifier.padding(padding)) {
                StreamingGrid(data = streamingData) { streaming ->
                    viewModel.saveSelectedStreaming(streaming)
                    navigate.toHome()
                }
            }
        }
    }
}

@Composable
fun StreamingGrid(
    data: StreamingData,
    onClick: (StreamingEntity) -> Unit
) {
    val padding = dimensionResource(R.dimen.default_padding)
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        columns = GridCells.Adaptive(minSize = dimensionResource(R.dimen.streaming_item_big_size)),
        verticalArrangement = Arrangement.spacedBy(padding),
        horizontalArrangement = Arrangement.spacedBy(padding)
    ) {
        streamingSession(
            top = { SimpleTitle(title = stringResource(R.string.main_steams)) },
            streamingEntities = data.mains,
            selectedId = data.selectedId,
            onClick = onClick
        )
        streamingSession(
            top = { SimpleTitle(title = stringResource(R.string.other_streams)) },
            streamingEntities = data.others,
            selectedId = data.selectedId,
            onClick = onClick
        )
    }
}

@Composable
fun StreamingItem(
    imageUri: String = "",
    painter: Painter? = null,
    isSelected: Boolean = false,
    contentDescription: String,
    onClick: () -> Unit
) {
    val modifier = Modifier.setStreamingIcon(isSelected, onClick)
    val contentScale = ContentScale.Fit

    Box {
        if (painter != null) {
            Image(
                painter = painter,
                modifier = modifier,
                contentScale = contentScale,
                contentDescription = contentDescription
            )
        } else {
            AsyncImage(
                model = createImageRequest(uri = imageUri),
                modifier = modifier,
                contentScale = contentScale,
                contentDescription = contentDescription,
                error = painterResource(R.drawable.placeholder)
            )
        }

        if (isSelected) {
            Icon(
                painterResource(id = R.drawable.radio_button_checked),
                contentDescription = null,
                tint = AccentColor,
                modifier = Modifier
                    .padding(5.dp)
                    .size(15.dp)
                    .background(PrimaryBackground.copy(0.7f), CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun ToolBar(onBackstack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.select_streaming),
            color = AccentColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.default_padding)
                    )
                    .clip(CircleShape)
                    .background(SecondaryBackground)
                    .onClick(onBackstack)
            ) {
                Icon(
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.keyboard_arrow_up),
                    contentDescription = stringResource(id = R.string.backstack_icon),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.icon_medium_size))
                )
            }
        }
    }
}

@Composable
private fun Modifier.setStreamingIcon(isSelected: Boolean, onClick: () -> Unit): Modifier {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.corner))
    return background(SecondaryBackground)
        .border(
            width = if (isSelected) 3.dp else 1.dp,
            color = if (isSelected) AccentColor else Gray,
            shape = shape
        )
        .clip(shape)
        .clickable { onClick() }
}

private fun LazyGridScope.streamingSession(
    top: @Composable () -> Unit,
    streamingEntities: List<StreamingEntity>,
    selectedId: Long,
    onClick: (StreamingEntity) -> Unit
) {
    if (streamingEntities.isNotEmpty()) {
        item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                top()
            }
        }
        items(streamingEntities.size) { index ->
            with(streamingEntities[index]) {
                StreamingItem(
                    imageUri = getLogoImage(),
                    contentDescription = name,
                    isSelected = apiId == selectedId,
                    onClick = { onClick(this) }
                )
            }
        }
    }
}

@Composable
private fun createImageRequest(uri: String) =
    ImageRequest.Builder(LocalContext.current)
        .data(data = uri)
        .crossfade(true)
        .build()

@Preview
@Composable
fun StreamingItemNotActivatedPreview() {
    StreamingItem(
        painter = painterResource(id = R.drawable.netflix_icon),
        contentDescription = "Netflix",
        isSelected = false
    ) {}
}

@Preview
@Composable
fun StreamingItemActivatedPreview() {
    StreamingItem(
        painter = painterResource(id = R.drawable.netflix_icon),
        contentDescription = "Netflix",
        isSelected = true
    ) {}
}
