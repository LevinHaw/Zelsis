package com.submission.zelsis.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateFormat {
    companion object {
        fun formatDate(
            dateString: String, inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            outputPattern: String = "EEE, d MMM yyyy"): String {

            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()

            return try {
                val date = inputFormat.parse(dateString)
                outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}