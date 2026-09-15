package br.dev.singular.overview.presentation.ui.screens.user.profile.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginIntent

@Immutable
data class ProfileActions(
    val tagPath: String = "/profile",
    private val navigation: INavigationWrapper? = null,
    val handleIntent: (LoginIntent) -> Unit = {}
) {

    fun onLogoutRequest() {
        TagManager.logClick(customPath = tagPath, detail = "sign-out")
    }

    fun onLogout() {
        TagManager.logClick(customPath = tagPath, detail = "sign-out-confirm")
        handleIntent(LoginIntent.Logout)
    }

    fun onLogoutCancel() {
        TagManager.logClick(customPath = tagPath, detail = "sign-out-cancel")
    }

    fun onSignedOut() {
        navigation?.popBackStack()
    }

    fun onBack() {
        TagManager.logClick(customPath = tagPath, detail = TagCommon.Detail.BACK)
        navigation?.popBackStack()
    }
}
