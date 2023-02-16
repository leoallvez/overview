package br.com.deepbyte.overview.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DateHelper(dateIn: String?) {

    private val date: String = dateIn ?: DEFAULT_RETURN

    fun getYear(): String = if (date.isNotEmpty()) {
        date.split(DATE_SEPARATOR).first()
    } else {
        DEFAULT_RETURN
    }

    fun formattedDate(): String = if (date.isNotEmpty()) {
        val format = SimpleDateFormat(API_DATE_PATTERN, Locale.getDefault())
        format.parse(date)?.let { dateFormat(it) } ?: DEFAULT_RETURN
    } else {
        DEFAULT_RETURN
    }

    private fun dateFormat(date: Date): String {
        val locale = Locale.getDefault()
        val datePattern = if (locale.language == "pt") "dd/MM/yyyy" else "MM/dd/yyyy"
        return SimpleDateFormat(datePattern, locale).format(date)
    }

    fun periodBetween(dateEnd: String?): String = if (date.isNotEmpty()) {
        calculatePeriod(
            start = date.toCalendar(),
            end = dateEnd?.toCalendar() ?: Calendar.getInstance()
        )
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
        val today = Calendar.getInstance()
        date.toCalendar() > today
    } else {
        false
    }

    private fun String.toCalendar() = try {
        if (isNotEmpty()) {
            val (year, mouth, day) = split(DATE_SEPARATOR).map { it.toInt() }
            Calendar.getInstance().apply { set(year, mouth, day) }
        } else {
            Calendar.getInstance()
        }
    } catch (e: NumberFormatException) {
        Timber.e(e, "error on parse date to calendar")
        Calendar.getInstance()
    }

    companion object {
        private const val DATE_SEPARATOR = "-"
        private const val DEFAULT_RETURN = ""
        private const val API_DATE_PATTERN = "yyyy-MM-dd"
    }
}
