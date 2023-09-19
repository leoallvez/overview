package br.dev.singular.overview

interface IAnalyticsTracker {
    fun screenViewEvent(screenName: String, className: String)
}
