package br.dev.singular.overview.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateHelper(dateIn: String?) {

    private val date: String = dateIn ?: DEFAULT_RETURN

    fun formattedDate(): String = if (date.isNotEmpty()) {
        val format = SimpleDateFormat(API_DATE_PATTERN, Locale.getDefault())
        format.parse(date)?.let { dateFormat(it) } ?: DEFAULT_RETURN
    } else {
        DEFAULT_RETURN
    }

    private fun dateFormat(date: Date): String {
        val locale = Locale.getDefault()
        val isLatinLanguage = locale.language == "pt" || locale.language == "es"
        val datePattern = if (isLatinLanguage) "dd/MM/yyyy" else "MM/dd/yyyy"
        return SimpleDateFormat(datePattern, locale).format(date)
    }

    fun periodBetween(dateEnd: String?): String = if (date.isNotEmpty()) {
        when (val start = date.toCalendar()) {
            null -> DEFAULT_RETURN
            else -> {
                val end = dateEnd?.toCalendar() ?: Calendar.getInstance()
                calculatePeriod(start = start, end = end)
            }
        }
    } else {
        DEFAULT_RETURN
    }

    private fun calculatePeriod(start: Calendar, end: Calendar): String {
        var diff = end.get(Calendar.YEAR) - start.get(Calendar.YEAR)
        if (end.get(Calendar.DAY_OF_YEAR) < start.get(Calendar.DAY_OF_YEAR)) {
            diff--
        }
        return diff.toString()
    }

    fun isFutureDate() = if (date.isNotEmpty()) {
        when (val date = date.toCalendar()) {
            null -> false
            else -> {
                val today = Calendar.getInstance()
                date > today
            }
        }
    } else {
        false
    }

    private fun String.toCalendar() = try {
        if (isNotEmpty()) {
            val (year, month, day) = split(DATE_SEPARATOR).map { it.toInt() }
            Calendar.getInstance().apply { set(year, month - 1, day) }
        } else {
            null
        }
    } catch (e: NumberFormatException) {
        null
    }

    companion object {
        private const val DATE_SEPARATOR = "-"
        private const val DEFAULT_RETURN = ""
    }
}
