package br.com.deepbyte.overview.ui.navigation.events

import androidx.navigation.NavController

open class BasicsMediaEvents(private val navigation: NavController) {

    fun onNavigateToHome() = br.com.deepbyte.overview.ui.navigation.onNavigateToHome(navigation).invoke()

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?) =
        br.com.deepbyte.overview.ui.navigation.onNavigateToMediaDetails(navigation)
            .invoke(apiId, mediaType)
}
