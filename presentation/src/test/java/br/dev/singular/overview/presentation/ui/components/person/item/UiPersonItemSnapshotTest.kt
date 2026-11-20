package br.dev.singular.overview.presentation.ui.components.person.item

import br.dev.singular.overview.presentation.ui.components.UiSnapshotTest
import br.dev.singular.overview.presentation.ui.components.person.UiPersonItemLongTextPreview
import br.dev.singular.overview.presentation.ui.components.person.UiPersonItemPreview
import br.dev.singular.overview.presentation.ui.components.person.UiPersonItemSkeletonPreview
import org.junit.Test

class UiPersonItemSnapshotTest : UiSnapshotTest(snapshotPackage = "components/person/item") {

    @Test
    fun default() = snapshot {
        UiPersonItemPreview()
    }

    @Test
    fun longText() = snapshot {
        UiPersonItemLongTextPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiPersonItemSkeletonPreview()
    }
}
