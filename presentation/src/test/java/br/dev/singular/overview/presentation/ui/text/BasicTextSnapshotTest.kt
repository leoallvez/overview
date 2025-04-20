package br.dev.singular.overview.presentation.ui.text

import BaseSnapshotTest
import android.R
import androidx.compose.ui.graphics.Color
import org.junit.Test

class BasicTextSnapshotTest : BaseSnapshotTest() {

    @Test
    fun filled() {
        paparazzi.snapshot {
            BasicText(text = "Lorem ipsum dolor sit amet")
        }
    }

    @Test
    fun isBold() {
        paparazzi.snapshot {
            BasicText(text = "Lorem ipsum dolor sit amet", isBold = true)
        }
    }

    @Test
    fun changeColor() {
        paparazzi.snapshot {
            BasicText(text = "Lorem ipsum dolor sit amet", color = Color.Yellow)
        }
    }
}
