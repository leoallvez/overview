package io.github.leoallvez.take.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.leoallvez.take.ui.theme.Background

@Composable
fun DiscoverScreen() {
    Column(
        modifier = Modifier
            .background(Background)
            .fillMaxSize()
    ) {
        Box {
            Text(text = "Discover screen", modifier = Modifier.align(Alignment.Center))
        }
    }
}