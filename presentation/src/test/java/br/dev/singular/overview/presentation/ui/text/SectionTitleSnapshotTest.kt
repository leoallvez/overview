package br.dev.singular.overview.presentation.ui.text

import BaseSnapshotTest
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.junit.Test

class SectionTitleSnapshotTest : BaseSnapshotTest() {

    @Test
    fun filled() = snapshot {
        SectionTitle(text = "Lorem ipsum dolor sit amet")
    }

    @Test
    fun changeColor() = snapshot {
        SectionTitle(text = "Lorem ipsum dolor sit amet", color = Color.Yellow)
    }

    @Test
    fun withModifier() = snapshot {
        SectionTitle(
            text = "Lorem ipsum dolor sit amet",
            color = Color.Black,
            modifier = Modifier.background(Color.White)
        )
    }
}
