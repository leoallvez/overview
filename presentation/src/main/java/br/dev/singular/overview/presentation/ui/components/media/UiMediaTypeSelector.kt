package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.components.UiChip

@Composable
fun UiMediaTypeSelector(
    type: MediaUiType,
    modifier: Modifier = Modifier,
    onClick: (MediaUiType) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement
            .spacedBy( dimensionResource(R.dimen.spacing_2x))
    ) {
        items(items = MediaUiType.entries) {
            UiChip(
                text = stringResource(it.labelRes),
                activated = it == type
            ) {
                onClick.invoke(it)
            }
        }
    }
}

@Preview(name = "English", locale = "en")
@Composable
internal fun UiMediaTypeSelectorEnglishPreview() {
    UiMediaTypeSelectorPreviewHelper()
}

@Preview(name = "Portuguese", locale = "pt-rBR")
@Composable
internal fun UiMediaTypeSelectorPortuguesePreview() {
    UiMediaTypeSelectorPreviewHelper()
}

@Preview(name = "Japanese", locale = "ja")
@Composable
internal fun UiMediaTypeSelectorJapanesePreview() {
    UiMediaTypeSelectorPreviewHelper()
}

@Preview(name = "Spanish", locale = "es")
@Composable
internal fun UiMediaTypeSelectorSpanishPreview() {
    UiMediaTypeSelectorPreviewHelper()
}

@Composable
private fun UiMediaTypeSelectorPreviewHelper() {
    var type  by remember { mutableStateOf(MediaUiType.ALL) }
    UiMediaTypeSelector(
        type,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
    ) { newType ->
        type = newType
    }
}
