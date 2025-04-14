package br.dev.singular.overview.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.dev.singular.overview.presentation.model.MediaUIModel

//@Composable
//fun MediaItem(media: MediaUIModel, imageWithBorder: Boolean = false, onClick: () -> Unit) {
//    Column(Modifier.clickable { onClick.invoke() }) {
//        BasicImage(
//            url = media.posterPath,
//            contentDescription = mediaItem.getLetter(),
//            withBorder = imageWithBorder
//        )
//        BasicText(
//            text = mediaItem.getLetter(),
//            style = MaterialTheme.typography.bodySmall,
//            isBold = true
//        )
//    }
//}