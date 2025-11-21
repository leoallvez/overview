package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor

@Composable
fun UiTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x))
    ) {
        UiTitle(
            text = title,
            modifier = Modifier.align(Alignment.CenterStart),
            color = HighlightColor
        )
    }
}

@Preview
@Composable
fun UiToolbarPreview() {
    UiTopAppBar(title = "Title")
}