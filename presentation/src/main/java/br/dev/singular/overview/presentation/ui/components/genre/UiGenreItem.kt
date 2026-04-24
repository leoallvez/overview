package br.dev.singular.overview.presentation.ui.components.genre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import br.dev.singular.overview.presentation.ui.utils.fakeGenres
import br.dev.singular.overview.presentation.ui.utils.getImageVector
import br.dev.singular.overview.presentation.ui.utils.localizedName

/**
 * A composable that displays a genre item with a selection radio button.
 *
 * @param model The [GenreUiModel] to display.
 * @param selected The selection state of the item.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the item is clicked.
 */
@Composable
fun UiGenreItem(
    model: GenreUiModel,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.corner_width))
    val color = if (selected) HighlightColor else DefaultTextColor
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_18x))
            .border(
                style = UiBorderStyle(
                    visible = selected,
                    color = HighlightColor,
                    shape = shape
                )
            )
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_3x)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.spacing_12x))
                    .clip(shape)
                    .border(style = UiBorderStyle(shape = shape)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = model.getImageVector(),
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_6x)),
                    tint = color
                )
            }

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_3x)))

            UiTitle(
                text = model.localizedName(),
                modifier = Modifier.weight(1f),
                color = color
            )
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors().copy(
                    selectedColor = HighlightColor,
                    unselectedColor = LowlightColor,
                )
            )
        }
    }
}

@UiComponentPreview
@Composable
private fun UiGenreItemPreview() {
    var selected by remember { mutableStateOf(false) }
    UiGenreItem(
        model = fakeGenres().first().copy(name = stringResource(R.string.lorem_ipsum)),
        selected = selected
    ) {
        selected = !selected
    }
}
