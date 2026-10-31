package br.dev.singular.overview.presentation.ui.screens.episodes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBarBackPosition.LEADING
import br.dev.singular.overview.presentation.ui.components.text.UiParagraph
import br.dev.singular.overview.presentation.ui.screens.episodes.interaction.EpisodesActions
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview

/**
 * The entry point for the Episodes screen of a TV show.
 * It is a placeholder until the episode list is implemented.
 *
 * @param tvShowId The unique identifier of the TV show whose episodes are listed.
 * @param actions The actions that can be performed on this screen.
 */
@Composable
fun EpisodesScreen(
    tvShowId: Long,
    actions: EpisodesActions = EpisodesActions()
) {
    UiScaffold(
        topBar = {
            UiTopAppBar(
                title = "Episodes",
                backPosition = LEADING,
                onBack = actions::onBack
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            UiParagraph(text = "The episodes of TV show $tvShowId will be implemented here.")
        }
    }
}

@UiScreenPreview
@Composable
internal fun EpisodesScreenPreview() {
    EpisodesScreen(tvShowId = 1L)
}
