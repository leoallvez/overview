package br.dev.singular.overview.presentation.ui.components.person.list

import br.dev.singular.overview.presentation.ui.components.UiScreenSnapshotTest
import br.dev.singular.overview.presentation.ui.components.person.UiPersonListEmptyPreview
import br.dev.singular.overview.presentation.ui.components.person.UiPersonListPreview
import br.dev.singular.overview.presentation.ui.components.person.UiPersonListSkeletonPreview
import org.junit.Test

class UiPersonListSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "components/person/list") {

    @Test
    fun default() = snapshot {
        UiPersonListPreview()
    }

    @Test
    fun empty() = snapshot {
        UiPersonListEmptyPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiPersonListSkeletonPreview()
    }
}
