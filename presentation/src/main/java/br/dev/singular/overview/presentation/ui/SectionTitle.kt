package br.dev.singular.overview.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun SectionTitlePreview() {
    Column {
        SectionTitle(text = "Title One")
        SectionTitle(text = "Title Two", color = Color.Gray)
        SectionTitle(
            text = "Title Three",
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = dimensionResource(R.dimen.spacing_s)),
            color = Color.Black
        )
    }
}
