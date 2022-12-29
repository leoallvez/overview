package br.com.deepbyte.overview

interface IAnalyticsTracker {
    fun screenViewEvent(screenName: String, className: String)
}
