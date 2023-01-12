package br.com.deepbyte.overview.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Date

class DateHelper(dateIn: String?) {

    private val date: String = dateIn ?: DEFAULT_RETURN

    fun getYear(): String = if (date.isNotEmpty()) {
        date.split("-").first()
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

    fun periodBetween(dateEnd: String? = null): String = if (date.isNotEmpty()) {
        val formatter = DateTimeFormatter.ofPattern(API_DATE_PATTERN)
        val start = LocalDate.parse(date, formatter)
        val end = if (dateEnd.isNullOrBlank().not()) {
            LocalDate.parse(dateEnd, formatter)
        } else {
            LocalDate.now()
        }
        Period.between(start, end).years.toString()
    } else {
        DEFAULT_RETURN
    }

    companion object {
        private const val DEFAULT_RETURN = ""
        private const val API_DATE_PATTERN = "yyyy-MM-dd"
    }
}
