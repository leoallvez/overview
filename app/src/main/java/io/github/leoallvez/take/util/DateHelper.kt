package io.github.leoallvez.take.util

import java.text.SimpleDateFormat
import java.util.*

class DateHelper(dateIn: String?) {

    private val date: String = dateIn ?: DEFAULT_RETURN

    fun getYear(): String = if (date.isNotEmpty()) {
        date.split("-").first()
    } else {
        DEFAULT_RETURN
    }

    fun formattedDate(): String = if (date.isNotEmpty()) {
        val locale = Locale.getDefault()
        val format = SimpleDateFormat("yyyy-MM-dd", locale)
        format.parse(date)?.let { dateFormat(locale, it) } ?: DEFAULT_RETURN
    } else {
        DEFAULT_RETURN
    }

    private fun dateFormat(locale: Locale, date: Date) =
        SimpleDateFormat(datePattern(locale), locale).format(date)

    private fun datePattern(locale: Locale) =
        if (locale.language == "pt") "dd/MM/yyyy" else "MM/dd/yyyy"

    companion object {
        private const val DEFAULT_RETURN = ""
    }
}
