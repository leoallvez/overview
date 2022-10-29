package io.github.leoallvez.take.ui.person

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
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
import io.github.leoallvez.take.data.MediaType.MOVIE
import io.github.leoallvez.take.data.MediaType.TV
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.PersonImageCircle
import io.github.leoallvez.take.ui.ErrorScreen
import io.github.leoallvez.take.ui.ToolbarButton
import io.github.leoallvez.take.ui.ScreenNav
import io.github.leoallvez.take.ui.TrackScreenView
import io.github.leoallvez.take.ui.ScreenTitle
import io.github.leoallvez.take.ui.BasicParagraph
import io.github.leoallvez.take.ui.AdsBanner
import io.github.leoallvez.take.ui.SimpleSubtitle
import io.github.leoallvez.take.ui.PartingEmDash
import io.github.leoallvez.take.ui.PartingPoint
import io.github.leoallvez.take.ui.UiStateResult
import io.github.leoallvez.take.ui.MediaItemList
import io.github.leoallvez.take.ui.theme.PrimaryBackground
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
    viewModel: PersonDetailsViewModel = hiltViewModel()
) {
    val showAds = viewModel.adsAreVisible().observeAsState(initial = false).value

    TrackScreenView(screen = ScreenNav.CastDetails, logger)

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
    onRefresh: () -> Unit
) {
    if (person == null) {
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
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner)))
            .background(PrimaryBackground)
    ) {

        PersonImageCircle(
            imageUrl = person.getProfileImage(),
            contentDescription = person.name,
            modifier = Modifier
                .size(300.dp)
                .padding(dimensionResource(R.dimen.screen_padding))
                .align(Alignment.Center)
        )
        ToolbarButton(
            painter = Icons.Default.KeyboardArrowLeft,
            descriptionResource = R.string.back_to_home_icon,
            background = Color.White.copy(alpha = 0.1f)
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
            .padding(dimensionResource(R.dimen.default_padding))
    ) {
        person.apply {
            ScreenTitle(name)
            PersonDates(person)
            PlaceOfBirth(birthPlace())
            BasicParagraph(R.string.biography, biography)
            ParticipationList(R.string.movies_participation, getFilmography(), MOVIE, onClickItem)
            ParticipationList(R.string.tv_shows_participation, getTvShows(), TV, onClickItem)
            AdsBanner(R.string.banner_sample_id, showAds)
        }
    }
}

@Composable
fun PersonDates(person: Person) {
    person.apply {
        if (getFormattedBirthday().isNotEmpty()) {
            Row(Modifier.padding(vertical = 10.dp)) {
                SimpleSubtitle(getFormattedBirthday())
                PersonDeathDay(getFormattedDeathDay())
                PersonAge(getAge())
            }
        }
    }
}

@Composable
fun PlaceOfBirth(placeOfBirth: String) {
    if (placeOfBirth.isNotEmpty()) {
        Column {
            SimpleSubtitle(stringResource(R.string.place_of_birth), isBold = true)
            BasicParagraph(placeOfBirth)
        }
    }
}

@Composable
fun PersonDeathDay(deathDay: String) {
    if (deathDay.isNotEmpty()) {
        PersonSpace()
        PartingEmDash()
        PersonSpace()
        SimpleSubtitle(deathDay)
    }
}

@Composable
fun PersonAge(age: String) {
    if (age.isNotEmpty()) {
        PersonSpace()
        PartingPoint()
        PersonSpace()
        Text(
            text = stringResource(R.string.age, age),
            color = Color.White,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun PersonSpace() {
    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
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
        items = mediaItems
    ) { apiId, _ ->
        onClickItem.invoke(apiId, mediaType.key)
    }
}