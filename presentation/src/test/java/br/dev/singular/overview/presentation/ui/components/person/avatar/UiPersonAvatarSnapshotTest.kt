package br.dev.singular.overview.presentation.ui.components.person.avatar

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.person.UiPersonAvatarPreview
import org.junit.Test

class UiPersonAvatarSnapshotTest : UiSnapshotTest(snapshotPackage = "components/person/avatar") {

    @Test
    fun default() = snapshot {
        UiPersonAvatarPreview()
    }
}
