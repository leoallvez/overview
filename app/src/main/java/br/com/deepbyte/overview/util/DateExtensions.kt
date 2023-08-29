package br.com.deepbyte.overview.util

import java.text.SimpleDateFormat
import java.util.Date

const val API_DATE_PATTERN = "yyyy-MM-dd"

fun Date.toFormatted(): String = SimpleDateFormat(API_DATE_PATTERN).format(this)

fun Date.toLastWeekFormatted() = Date(this.time - (1000 * 60 * 60 * 24 * 7)).toFormatted()
