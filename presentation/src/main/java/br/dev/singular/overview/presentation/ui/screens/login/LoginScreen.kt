package br.dev.singular.overview.presentation.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.UiInfoTooltip
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.button.UiGoogleButton
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview

@Composable
fun LoginScreen(
    onLogin: () -> Unit = {}
) {
    UiScaffold(
        topBar = { UiTopAppBar(title = stringResource(R.string.login)) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            UiInfoTooltip(
                message = stringResource(R.string.lorem_ipsum_long),
            )
            UiGoogleButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = onLogin
            )
        }
    }
}

@UiScreenPreview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
