package br.dev.singular.overview.presentation.ui.screens.person

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.ui.components.UiAdsMediumRectangle
import br.dev.singular.overview.presentation.ui.components.UiPersonAvatar
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.media.UiMediaList
import br.dev.singular.overview.presentation.ui.components.text.UiParagraph
import br.dev.singular.overview.presentation.ui.components.text.UiSubtitle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.screens.common.ErrorScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult
import br.dev.singular.overview.presentation.ui.screens.person.interaction.PersonDetailsActions
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.defaultBackground
import br.dev.singular.overview.presentation.ui.utils.fakePerson
import kotlinx.collections.immutable.ImmutableList

/**
 * A screen that displays the details of a person.
 *
 * @param personId The ID of the person to display.
 * @param uiState The state of the UI, which can be loading, success, or error.
 * @param showAds Whether to show ads on the screen.
 * @param actions The actions to be performed on the screen.
 */
@Composable
fun PersonDetailsScreen(
    personId: Long,
    uiState: UiState<PersonUiModel?>,
    showAds: Boolean = false,
    actions: PersonDetailsActions,
) {
    LaunchedEffect(Unit) { actions.onLoad(personId) }

    UiStateResult(
        uiState = uiState,
        tagPath = actions.tagPath,
        onRefresh = { actions.onLoad(personId) }
    ) { person ->
        if (person == null) {
            ErrorScreen(actions.tagPath, onRefresh = { actions.onLoad(personId) })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultBackground()
                    .verticalScroll(rememberScrollState())
            ) {
                PersonToolBar(person, actions)
                PersonBody(person, showAds, actions)
            }
        }
    }
}

@Composable
private fun PersonToolBar(
    person: PersonUiModel,
    actions: PersonDetailsActions
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(292.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_width)))
            .padding(top = dimensionResource(R.dimen.spacing_4x))
            .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    ) {
        UiPersonAvatar(
            url = person.profileURL,
            modifier = Modifier.align(Alignment.Center),
            previewDrawableRes = person.previewDrawableRes,
            size = dimensionResource(R.dimen.avatar_large)
        )
        UiIconButton(
            iconStyle = UiIconStyle(
                source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                descriptionRes = R.string.backstack_icon,
            ),
            background = Color.White.copy(alpha = 0.1f),
            onClick = { actions.onBack() }
        )
    }
}

@Composable
private fun PersonBody(
    person: PersonUiModel,
    showAds: Boolean,
    actions: PersonDetailsActions
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .defaultBackground()
    ) {
        with(person) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.spacing_4x),
                        vertical = dimensionResource(R.dimen.spacing_2x)
                    )
            ) {
                UiTitle(text = name, color = HighlightColor)
                Dates(person)
                PlaceOfBirth(placeOfBirth)
                Biography(biography)
                UiAdsMediumRectangle(R.string.person_banner, isVisible = showAds)
            }
            Participation(
                listTitleRes = R.string.movies_participation,
                actions = actions,
                medias = movies,
            )
            Participation(
                listTitleRes = R.string.tv_shows_participation,
                actions = actions,
                medias = tvShows,
            )
        }
    }
}

@Composable
private fun Dates(person: PersonUiModel) = with(person) {

    val lifespan = when {
        deathDay.isNotEmpty() -> {
            stringResource(R.string.em_dash, birthday, deathDay)
        }

        else -> birthday
    }

    val fullCaption = when {
        age.isNotEmpty() -> {
            val formatedAge = stringResource(R.string.age, age)
            stringResource(R.string.separator, lifespan, formatedAge)
        }

        else -> lifespan
    }

    UiSubtitle(text = fullCaption, isBold = true)
}

@Composable
private fun Biography(biography: String) {
    if (biography.isNotEmpty()) {
        Column {
            UiTitle(stringResource(R.string.biography))
            UiParagraph(biography)
        }
    }
}

@Composable
private fun PlaceOfBirth(placeOfBirth: String) {
    if (placeOfBirth.isNotEmpty()) {
        Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_1x))) {
            UiSubtitle(stringResource(R.string.place_of_birth), isBold = true)
            UiText(placeOfBirth)
        }
    }
}

@Composable
private fun Participation(
    @StringRes listTitleRes: Int,
    medias: ImmutableList<MediaUiModel>,
    actions: PersonDetailsActions
) {
    UiMediaList(
        title = stringResource(listTitleRes),
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
        items = medias,
        onClick = { actions.onToMediaDetails(it) }
    )
}

@UiScreenPreview
@Composable
private fun PersonDetailsScreenPreview() {
    PersonDetailsScreen(
        personId = 1L,
        uiState = UiState.Success(data = fakePerson()),
        actions = PersonDetailsActions()
    )
}
