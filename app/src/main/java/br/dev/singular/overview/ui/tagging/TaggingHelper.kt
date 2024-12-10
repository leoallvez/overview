package br.dev.singular.overview.ui.tagging

import android.os.Bundle
import br.dev.singular.overview.ui.tagging.constants.common.TaggingEvents
import br.dev.singular.overview.ui.tagging.constants.common.TaggingParams
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber
import javax.inject.Inject


interface ITaggingHelper {
    fun logScreenView(customPath: String)
    fun logClick(customPath: String, detail: String)
}

class TaggingHelper @Inject constructor(
    private val analytics: FirebaseAnalytics
) : ITaggingHelper {

    override fun logScreenView(customPath: String) {
        logEvent(TaggingEvents.SCREEN_VIEW, Bundle().apply {
            putString(TaggingParams.CUSTOM_PATH, customPath)
        })
    }

    override fun logClick(customPath: String, detail: String) {
        logEvent(TaggingEvents.CLICK, Bundle().apply {
            putString(TaggingParams.CUSTOM_PATH, customPath)
            putString(TaggingParams.DETAIL, detail)
        })
    }

    private fun logEvent(name: String, bundle: Bundle) {
        Timber.d("analytics - name:$name, bundle:$bundle")
        analytics.logEvent(name, bundle)
    }
}
