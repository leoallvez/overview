package br.com.deepbyte.overview.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

const val API_DATE_PATTERN = "yyyy-MM-dd"

fun Date.toFormatted(): String = SimpleDateFormat(API_DATE_PATTERN).format(this)

// write an extension method that takes a Date back one month
fun Date.toLastMonthFormatted(): String {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MONTH, -1)
    return cal.time.toFormatted()
}
