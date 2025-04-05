package br.dev.singular.overview.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SampleText(text: String) {
    Text(text = text, color = Color.White)
}

@Preview
@Composable
fun PreviewSample() {
    SampleText("My sample")
}
