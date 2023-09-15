package dev.com.singular.overview

interface IAnalyticsTracker {
    fun screenViewEvent(screenName: String, className: String)
}
