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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.tagging.params.TagPerson
import br.dev.singular.overview.presentation.ui.components.media.UiMediaList
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.ui.AdsMediumRectangle
import br.dev.singular.overview.ui.BasicParagraph
import br.dev.singular.overview.ui.ButtonWithIcon
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.PartingEmDash
import br.dev.singular.overview.ui.PartingPoint
import br.dev.singular.overview.ui.PersonImageCircle
import br.dev.singular.overview.ui.SimpleSubtitle1
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.PrimaryBackground
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.dev.singular.overview.presentation.model.MediaUiModel

@Composable
fun PersonDetailsScreen(
    apiId: Long,
    navigate: BasicNavigate,
    viewModel: PersonDetailsViewModel = hiltViewModel()
) {
    val onRefresh = { viewModel.load(apiId) }
    LaunchedEffect(true) {
        onRefresh.invoke()
    }

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        tagPath = TagPerson.PATH,
        onRefresh = onRefresh
    ) { dataResult ->
        PersonDetailsContent(
            person = dataResult,
            showAds = viewModel.showAds,
            onRefresh = onRefresh::invoke,
            onBackstackClick = navigate::popBackStack,
            onBackstackLongClick = navigate::toHome,
            onNavigateToMediaDetails = navigate::toMediaDetails
        )
    }
}

@Composable
fun PersonDetailsContent(
    person: Person?,
    showAds: Boolean,
    onRefresh: () -> Unit,
    onBackstackClick: () -> Unit,
    onBackstackLongClick: () -> Unit,
    onNavigateToMediaDetails: (MediaUiModel) -> Unit
) {
    if (person == null) {
        ErrorScreen(TagPerson.PATH) { onRefresh.invoke() }
    } else {
        CollapsingToolbarScaffold(
            modifier = Modifier.background(PrimaryBackground),
            scrollStrategy = ScrollStrategy.EnterAlways,
            state = rememberCollapsingToolbarScaffoldState(),
            toolbar = {
                PersonToolBar(
                    person = person,
                    onBackstackClick = onBackstackClick::invoke,
                    onBackstackLongClick = onBackstackLongClick::invoke
                )
            }
        ) {
            PersonBody(person, showAds, onNavigateToMediaDetails::invoke)
        }
    }
}

@Composable
fun PersonToolBar(
    person: Person,
    onBackstackClick: () -> Unit,
    onBackstackLongClick: () -> Unit
) {
    val tagBack = { TagManager.logClick(TagPerson.PATH, TagCommon.Detail.BACK) }
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_width)))
            .background(PrimaryBackground)
    ) {
        PersonImageCircle(
            person,
            modifier = Modifier
                .size(300.dp)
                .padding(dimensionResource(R.dimen.spacing_small))
                .align(Alignment.Center)
        )
        ButtonWithIcon(
            painter = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            descriptionResource = R.string.backstack_icon,
            background = Color.White.copy(alpha = 0.1f),
            onClick = {
                tagBack.invoke()
                onBackstackClick.invoke()
            },
            onLongClick = {
                tagBack.invoke()
                onBackstackLongClick.invoke()
            }
        )
    }
}

@Composable
fun PersonBody(
    person: Person,
    showAds: Boolean,
    onClickItem: (MediaUiModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PrimaryBackground)
    ) {
        person.apply {
            UiTitle(
                text = name,
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.spacing_small)),
                color = AccentColor
            )
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
                    horizontal = dimensionResource(R.dimen.spacing_small)
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
                horizontal = dimensionResource(R.dimen.spacing_small)
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
            style = MaterialTheme.typography.titleMedium
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
    medias: List<MediaUiModel>,
    onClickItem: (MediaUiModel) -> Unit
) {
    UiMediaList(
        title = stringResource(listTitleRes),
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_small)),
        items = medias,
        onClick = {
            TagMediaManager.logClick(TagPerson.PATH, it.id)
            onClickItem.invoke(it)
        },
    )
}
