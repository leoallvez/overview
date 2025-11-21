package br.dev.singular.overview.presentation.ui.screens.person

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.tagging.params.TagPerson
import br.dev.singular.overview.presentation.ui.components.UiAdsMediumRectangle
import br.dev.singular.overview.presentation.ui.components.UiImage
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
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.border
import br.dev.singular.overview.presentation.ui.utils.defaultBackground
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.String

@Composable
fun PersonDetailsScreen(
    tagPath: String= TagPerson.PATH,
    uiState: UiState<PersonUiModel?>,
    showAds: Boolean,
    onLoad: () -> Unit,
    onBack: () -> Unit,
    onToMediaDetails: (MediaUiModel) -> Unit,
) {
    LaunchedEffect(Unit) { onLoad() }

    UiStateResult(
        uiState = uiState,
        tagPath = tagPath,
        onRefresh = onLoad
    ) { person ->
        if (person == null) {
            ErrorScreen(tagPath, onRefresh = onLoad)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultBackground()
                    .verticalScroll(rememberScrollState())
            ) {
                PersonToolBar(tagPath, person, onBack)
                PersonBody(tagPath, person, showAds, onToMediaDetails)
            }
        }
    }
}

@Composable
fun PersonToolBar(
    tagPath: String,
    person: PersonUiModel,
    onBack: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(292.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_width)))
            .padding(top = dimensionResource(R.dimen.spacing_4x))
            .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    ) {
        PersonImage(
            person = person,
            modifier = Modifier.align(Alignment.Center)
        )
        UiIconButton(
            iconStyle = UiIconStyle(
                source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                descriptionRes = R.string.backstack_icon,
            ),
            background = Color.White.copy(alpha = 0.1f),
            onClick = {
                TagManager.logClick(tagPath, TagCommon.Detail.BACK)
                onBack.invoke()
            }
        )
    }
}

@Composable
private fun PersonImage(
    person: PersonUiModel,
    modifier: Modifier
) {
    UiImage(
        url = person.profileURL,
        contentScale = ContentScale.Crop,
        previewPainter = person.previewContent,
        modifier = modifier
            .size(268.dp)
            .clip(CircleShape)
            .border(),
        placeholder = R.drawable.avatar,
        errorDefaultImage = R.drawable.avatar
    )
}

@Composable
private fun PersonBody(
    tagPath: String,
    person: PersonUiModel,
    showAds: Boolean,
    onToMediaDetails: (MediaUiModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().defaultBackground()) {
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
                tagPath = tagPath,
                medias = movies,
                onToMediaDetails = onToMediaDetails
            )
            Participation(
                listTitleRes = R.string.tv_shows_participation,
                tagPath = tagPath,
                medias = tvShows,
                onToMediaDetails = onToMediaDetails
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
    tagPath: String,
    @StringRes listTitleRes: Int,
    medias: ImmutableList<MediaUiModel>,
    onToMediaDetails: (MediaUiModel) -> Unit
) {
    UiMediaList(
        title = stringResource(listTitleRes),
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
        items = medias,
        onClick = {
            TagMediaManager.logMediaClick(tagPath, it.id)
            onToMediaDetails.invoke(it)
        }
    )
}

@Preview
@Composable
private fun PersonDetailsScreenPreview() {
    val person = PersonUiModel(
        id = 0,
        job = "Actor",
        age = "24",
        name = "Celeste Beaumont",
        birthday = "01/01/1982",
        deathDay = "01/01/2006",
        biography = stringResource(R.string.lorem_ipsum),
        character = "Himself",
        profileURL = "",
        previewContent = painterResource(R.drawable.sample_profile),
        placeOfBirth = "Modesto, California, USA",
        tvShows = getMediaMocks().toImmutableList(),
        movies = getMediaMocks().toImmutableList()
    )
    PersonDetailsScreen(
        uiState = UiState.Success(data = person),
        showAds = false,
        onLoad = {},
        onBack = {},
        onToMediaDetails = {}
    )
}
