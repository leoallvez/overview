package io.github.leoallvez.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event.*
import com.google.firebase.analytics.FirebaseAnalytics.Param.*

class AnalyticsWrapper(
    private val _firebaseAnalytics: FirebaseAnalytics?
) {

    fun logEvent(name: String, params: AnalyticsParams.Builder) {
        _firebaseAnalytics?.logEvent(name, params.build())
    }

    fun logEventSelectContent(params: AnalyticsParams.Builder) {
        _firebaseAnalytics?.logEvent(SELECT_ITEM, params.build())
    }

    fun setUserProperty(name: String, value: String) {
        _firebaseAnalytics?.setUserProperty(name, value)
    }

}

class AnalyticsParams {

    class Builder {

        private var id: Long? = null
        private var name: String? = null
        private var type: String? = null

        fun id(id: Long) = apply { this.id = id }

        fun name(name: String) = apply { this.name = name }

        fun type(type: String) = apply { this.type = type }

        fun build() = Bundle().apply {
            id?.let { this.putLong(ITEM_ID, it) }
            name?.let { this.putString(ITEM_NAME, it) }
            type?.let { this.putString(CONTENT_TYPE, it) }
        }
    }
}

fun main() {
    val params = AnalyticsParams.Builder()
        .id(100)
        .name("movie sample")
        .type("horror")

    val analytics = AnalyticsWrapper(null)

    analytics.logEvent("", params)


}