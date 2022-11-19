package br.com.deepbyte.overview

interface IAnalyticsTracker {
    fun logOpenScreen(screenName: String)
    fun logExitScreen(screenName: String)
}
