package br.dev.singular.overview.presentation.tagging

import br.dev.singular.overview.presentation.tagging.TagManager.Events.CLICK
import br.dev.singular.overview.presentation.tagging.TagManager.Events.INTERACTION
import br.dev.singular.overview.presentation.tagging.TagManager.Events.SCREEN_VIEW
import br.dev.singular.overview.presentation.tagging.TagManager.Params.CUSTOM_PATH
import br.dev.singular.overview.presentation.tagging.TagManager.Params.DETAIL
import br.dev.singular.overview.presentation.tagging.TagManager.Params.ITEM_ID
import br.dev.singular.overview.presentation.tagging.TagManager.Params.STATUS
import com.google.firebase.analytics.FirebaseAnalytics

interface ITagService {
    fun logScreenView(customPath: String, status: String = "")
    fun logClick(customPath: String, detail: String, id: Long = 0L)
    fun logInteraction(customPath: String, detail: String)
}

object TagManager : ITagService {

    private lateinit var analytics: FirebaseAnalytics

    fun init(instance: FirebaseAnalytics) {
        analytics = instance
    }

    override fun logScreenView(customPath: String, status: String) =
        logEvent(SCREEN_VIEW, mapOf(CUSTOM_PATH to customPath, STATUS to status))

    override fun logClick(customPath: String, detail: String, id: Long) =
        logEvent(
            CLICK,
            mapOf(
                CUSTOM_PATH to customPath,
                DETAIL to detail,
                ITEM_ID to id
            )
        )

    override fun logInteraction(customPath: String, detail: String) =
        logEvent(INTERACTION, mapOf(CUSTOM_PATH to customPath, DETAIL to detail))

    private fun logEvent(name: String, params: Map<String, Any>) {
        if (::analytics.isInitialized) analytics.logEvent(name, params.toBundle())
    }

    private object Events {
        const val SCREEN_VIEW = "screen_view"
        const val INTERACTION = "interaction"
        const val CLICK = "click"
    }

    private object Params {
        const val ITEM_ID = "item_id"
        const val DETAIL = "detail"
        const val STATUS = "status"
        const val CUSTOM_PATH = "custom_path"
    }
}
