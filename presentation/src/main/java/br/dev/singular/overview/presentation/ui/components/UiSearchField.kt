package br.dev.singular.overview.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X

@Composable
fun UiSearchField(
    query: String,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.search),
    onSearch: () -> Unit = {},
    onClear: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
) {
    val isEmpty by remember(query) { derivedStateOf { query.isEmpty() } }
    val borderColor by animateColorAsState(
        targetValue = if (isEmpty) LowlightColor.copy(alpha = 0.7f) else HighlightColor,
        label = "SearchFieldBorderColor"
    )

    Box(modifier = modifier.background(Background)) {
        BasicTextField(
            value = query,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.spacing_9x)),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = DefaultTextColor),
            onValueChange = onQueryChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            singleLine = true,
            cursorBrush = SolidColor(HighlightColor),
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .border(style = UiBorderStyle(color = borderColor))
                        .padding(start = dimensionResource(R.dimen.spacing_1x)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UiIcon(
                        source = UiIconSource.UiVector(Lucide.Search),
                        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_1x)),
                        contentDescription = stringResource(R.string.search_icon)
                    )
                    TextField(
                        placeholder = placeholder,
                        modifier = Modifier.weight(1f),
                        visible = isEmpty,
                        innerTextField = innerTextField
                    )
                    ClearIcon(
                        visible = isEmpty.not(),
                        onClick = {
                            onQueryChange("")
                            onClear()
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun TextField(
    placeholder: String,
    modifier: Modifier,
    visible: Boolean,
    innerTextField: @Composable (() -> Unit)
) {
    Box(
        modifier.padding(start = dimensionResource(R.dimen.spacing_1x)),
        contentAlignment = Alignment.CenterStart
    ) {
        if (visible) {
            Text(
                text = placeholder,
                style = LocalTextStyle.current.copy(
                    color = LowlightColor.copy(alpha = 0.7f),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            )
        }
        innerTextField()
    }
}

@Composable
private fun ClearIcon(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn()
                + expandHorizontally(expandFrom = Alignment.Start)
                + slideInHorizontally { -it / 2 },
        exit = fadeOut()
                + shrinkHorizontally(shrinkTowards = Alignment.Start)
                + slideOutHorizontally { it / 2 }
    ) {
        UiIconButton(
            iconStyle = UiIconStyle(
                color = HighlightColor,
                sizeRes = R.dimen.spacing_6x,
                source = UiIconSource.UiVector(Lucide.X),
                descriptionRes = R.string.close
            ),
            modifier = Modifier.padding(end = dimensionResource(R.dimen.spacing_1x)),
            borderStyle = UiBorderStyle(visible = false),
            background = Background,
            onClick = onClick
        )
    }
}

@UiComponentPreview
@Composable
private fun UiSearchFieldEmptyPreview() {
    val query = remember { mutableStateOf("") }
    UiSearchField(
        modifier = Modifier.padding(16.dp),
        query = query.value,
        onQueryChange = { query.value = it },
    )
}

@UiComponentPreview
@Composable
private fun UiSearchFieldWithQueryPreview() {
    val query = remember { mutableStateOf("Batman") }
    UiSearchField(
        modifier = Modifier.padding(16.dp),
        query = query.value,
        onQueryChange = { query.value = it },
    )
}
