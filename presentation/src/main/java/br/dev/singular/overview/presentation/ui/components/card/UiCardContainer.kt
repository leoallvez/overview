package br.dev.singular.overview.presentation.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.SubtitleTextColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border

/**
 * A bordered surface card, clickable as a whole, that stacks its content vertically.
 *
 * @param modifier The modifier to be applied to this component.
 * @param enabled Whether the card responds to clicks.
 * @param contentPadding The padding applied around the content.
 * @param onClick Callback invoked when the card is clicked.
 * @param content The content displayed inside the card.
 */
@Composable
internal fun UiCardContainer(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(dimensionResource(R.dimen.spacing_5x)),
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(
        size = dimensionResource(R.dimen.corner_width)
    )

    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .border(
                style = UiBorderStyle(
                    shape = shape
                )
            )
            .semantics { if (enabled) role = Role.Button },
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Surface,
            disabledContainerColor = Surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4x)),
            content = content
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiCardContainerPreview() {
    UiCardContainer(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    ) {
        UiText(
            text = "Lorem ipsum dolor sit amet",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            isBold = true
        )
        UiText(
            text = "Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore.",
            style = MaterialTheme.typography.bodyMedium,
            color = SubtitleTextColor,
            textAlign = TextAlign.Start,
            maxLines = Int.MAX_VALUE
        )
    }
}
