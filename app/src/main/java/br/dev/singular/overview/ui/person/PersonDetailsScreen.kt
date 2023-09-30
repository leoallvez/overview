package br.dev.singular.overview.ui.person

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.R
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.AdsMediumRectangle
import br.dev.singular.overview.ui.BasicParagraph
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.MediaItemList
import br.dev.singular.overview.ui.PartingEmDash
import br.dev.singular.overview.ui.PartingPoint
import br.dev.singular.overview.ui.PersonImageCircle
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.ScreenTitle
import br.dev.singular.overview.ui.SimpleSubtitle1
import br.dev.singular.overview.ui.ToolbarButton
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.MediaItemClick
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CastDetailsScreen(
    apiId: Long,
    navigate: BasicNavigate,
    viewModel: PersonDetailsViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.CastDetails, tracker = viewModel.analyticsTracker)

    viewModel.loadPersonDetails(apiId)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh(apiId) }
    ) { dataResult ->
        PersonDetailsContent(
            person = dataResult,
            showAds = viewModel.showAds,
            navigate::popBackStack,
            { apiId, mediaType ->
                navigate.toMediaDetails(apiId = apiId, mediaType = mediaType, backToHome = true)
            }
        ) {
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
            person,
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
    ) {
        person.apply {
            ScreenTitle(name)
            PersonDates(person)
            PlaceOfBirth(birthPlace())
            BasicParagraph(R.string.biography, biography)
            AdsMediumRectangle(R.string.person_banner, showAds)
            ParticipationList(R.string.movies_participation, getFilmography(), onClickItem)
            ParticipationList(R.string.tv_shows_participation, getTvShows(), onClickItem)
        }
    }
}

@Composable
fun PersonDates(person: Person) {
    person.apply {
        if (getFormattedBirthday().isNotEmpty()) {
            Row(
                modifier = Modifier.padding(
                    vertical = 10.dp,
                    horizontal = dimensionResource(R.dimen.screen_padding)
                )
            ) {
                SimpleSubtitle1(getFormattedBirthday())
                PersonDeathDay(getFormattedDeathDay())
                PersonAge(getAge())
            }
        }
    }
}

@Composable
fun PlaceOfBirth(placeOfBirth: String) {
    if (placeOfBirth.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.screen_padding)
            )
        ) {
            SimpleSubtitle1(stringResource(R.string.place_of_birth), isBold = true)
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
        SimpleSubtitle1(deathDay)
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
    medias: List<Media>,
    onClickItem: MediaItemClick
) {
    MediaItemList(
        items = medias,
        onClickItem = onClickItem,
        listTitle = stringResource(listTitleRes)
    )
}