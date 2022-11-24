package br.com.deepbyte.overview.ui.search

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ShowAds val showAds: Boolean
) : ViewModel()
