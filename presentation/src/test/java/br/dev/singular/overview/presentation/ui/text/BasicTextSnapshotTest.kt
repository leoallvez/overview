package br.dev.singular.overview.presentation.ui.text

import BaseSnapshotTest
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import org.junit.Test

class BasicTextSnapshotTest : BaseSnapshotTest() {

    @Test
    fun filled() = snapshot {
        BasicText(text = "Lorem ipsum dolor sit amet")
    }

    @Test
    fun isBold() = snapshot {
        BasicText(text = "Lorem ipsum dolor sit amet", isBold = true)
    }

    @Test
    fun changeColor() = snapshot {
        BasicText(text = "Lorem ipsum dolor sit amet", color = Color.Yellow)
    }

    @Test
    fun withModifier() = snapshot {
        BasicText(
            text = "Lorem ipsum dolor sit amet",
            color = Color.Black,
            modifier = Modifier.background(Color.White)
        )
    }

    @Test
    fun withStyle() = snapshot {
        BasicText(
            text = "Lorem ipsum dolor sit amet",
            style = TextStyle(fontStyle = Italic)
        )
    }
}
