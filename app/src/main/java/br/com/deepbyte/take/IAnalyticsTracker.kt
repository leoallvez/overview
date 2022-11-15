package br.com.deepbyte.take

interface IAnalyticsTracker {
    fun logOpenScreen(screenName: String)
    fun logExitScreen(screenName: String)
}
