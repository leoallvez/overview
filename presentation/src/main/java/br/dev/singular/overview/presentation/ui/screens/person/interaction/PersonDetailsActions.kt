package br.dev.singular.overview.presentation.ui.screens.person.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
data class PersonDetailsActions(
    private val navigation: INavigationWrapper? = null,
    val onLoad: (Long) -> Unit = {},
) {
    val tagPath: String = "/person-details"

    fun onBack() {
        TagManager.logClick(customPath = tagPath, detail = TagCommon.Detail.BACK)
        navigation?.popBackStack()
    }

    fun onToMediaDetails(media: MediaUiModel) {
        TagMediaManager.logMediaClick(tagPath, media.id)
        navigation?.toMediaDetails(media)
    }
}
