package br.dev.singular.overview.presentation.ui.components.media.selector

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelectorEnglishPreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelectorJapanesePreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelectorPortuguesePreview
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelectorSpanishPreview
import org.junit.Test

class UiMediaTypeSelectorSnapshotTest : UiSnapshotTest(
    snapshotPackage = "components/media/selector"
) {

    @Test
    fun english() = snapshot(locale = "en") {
        UiMediaTypeSelectorEnglishPreview()
    }

    @Test
    fun portuguese() = snapshot(locale = "pt-rBR") {
        UiMediaTypeSelectorPortuguesePreview()
    }

    @Test
    fun japanese() = snapshot(locale = "ja") {
        UiMediaTypeSelectorJapanesePreview()
    }

    @Test
    fun spanish() = snapshot(locale = "es") {
        UiMediaTypeSelectorSpanishPreview()
    }
}
