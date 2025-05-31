package br.dev.singular.overview.ui

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.remote.RemoteConfig

open class AdViewModel(adsManager: RemoteConfig<Boolean>) : ViewModel() {

    val showAds: Boolean by lazy { adsManager.execute() }

}
