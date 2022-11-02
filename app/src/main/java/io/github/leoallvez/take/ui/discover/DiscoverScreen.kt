package io.github.leoallvez.take.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import io.github.leoallvez.take.ui.theme.PrimaryBackground

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel = hiltViewModel()) {

    val pagingItems = viewModel.load(337).collectAsLazyPagingItems()

    Column(
        modifier = Modifier.background(PrimaryBackground).fillMaxSize()
    ) {

        LazyColumn {
            items(pagingItems) { item ->
                item?.let {
                    Text(text = item.getLetter())
                }
            }
        }
    }
}
