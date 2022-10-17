package io.github.leoallvez.take.ui.person

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.MediaType
import io.github.leoallvez.take.data.MediaType.*
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import io.github.leoallvez.take.ui.theme.SecondaryBackground
import io.github.leoallvez.take.util.MediaItemClick
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import io.github.leoallvez.take.data.api.response.PersonResponse as Person

@Composable
fun CastDetailsScreen(
    apiId: Long,
    logger: Logger,
    onNavigateToHome: () -> Unit,
    onNavigateToMediaDetails: MediaItemClick,
    viewModel: PersonDetailsViewModel = hiltViewModel(),
) {
    val showAds = viewModel.adsAreVisible().observeAsState(initial = false).value

    TrackScreenView(screen = Screen.CastDetails, logger)

    viewModel.loadPersonDetails(apiId)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh(apiId) }
    ) { dataResult ->
        PersonDetailsContent(dataResult, showAds, onNavigateToHome, onNavigateToMediaDetails) {
            viewModel.refresh(apiId)
        }
    }
}

@Composable
fun PersonDetailsContent(
    person: Person?,
    showAds: Boolean,
    onNavigateToHome: () -> Unit,
    onNavigateToMediaDetails: MediaItemClick,
    onRefresh: () -> Unit,
) {
    if(person == null) {
        ErrorScreen { onRefresh.invoke() }
    } else {
        CollapsingToolbarScaffold(
            modifier = Modifier.background(PrimaryBackground),
            scrollStrategy = ScrollStrategy.EnterAlways,
            state = rememberCollapsingToolbarScaffoldState(),
            toolbar = {
                PersonToolBar(person) {
                    onNavigateToHome.invoke()
                }
            }
        ) {
            PersonBody(person, showAds, onNavigateToMediaDetails)
        }
    }
}

@Composable
fun PersonToolBar(person: Person, backButtonAction: () -> Unit) {
    Box(
        Modifier.fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner)))
            .background(SecondaryBackground)
    ) {

        PersonImageCircle(
            imageUrl = person.getProfileImage(),
            contentDescription = person.name,
            modifier = Modifier
                .size(280.dp)
                .padding(dimensionResource(R.dimen.screen_padding))
                .align(Alignment.Center)
        )
        ToolbarButton(
            painter = Icons.Default.KeyboardArrowLeft,
            descriptionResource = R.string.back_to_home_icon
        ) { backButtonAction.invoke() }
    }
}

@Composable
fun PersonBody(
    person: Person,
    showAds: Boolean,
    onClickItem: MediaItemClick
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PrimaryBackground)
            .padding(dimensionResource(R.dimen.default_padding)),
    ) {
        person.apply {
            Text(text = getFormattedBirthday(), color = Color.White)
            ScreenTitle(name)
            BasicParagraph(R.string.biography, biography)
            ParticipationList(R.string.movies_participation, getFilmography(), MOVIE, onClickItem)
            ParticipationList(R.string.tv_shows_participation, getTvShows(), TV, onClickItem)
            AdsBanner(R.string.banner_sample_id, showAds)
        }
    }
}

@Composable
fun ParticipationList(
    @StringRes listTitleRes: Int,
    mediaItems: List<MediaItem>,
    mediaType: MediaType,
    onClickItem: MediaItemClick
) {
    MediaItemList(
        listTitle = stringResource(listTitleRes),
        items = mediaItems,
    ) { apiId, _ ->
        onClickItem.invoke(apiId, mediaType.key)
    }
}
