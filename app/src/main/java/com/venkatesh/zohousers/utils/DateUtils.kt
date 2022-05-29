package com.venkatesh.zohousers.utils

import java.text.ParseException
import java.text.SimpleDateFormat

class DateUtils {

    companion object {
        fun String.getDateWithServerTimeStamp(): String? {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val timeFormat = SimpleDateFormat("dd MMM yyyy")
            try {
                return timeFormat.format(dateFormat.parse(this)!!)
            } catch (e: ParseException) {
                return null
            }
        }

        fun String.getWeatherDateTime():String?{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val timeFormat = SimpleDateFormat("E, MMM dd, h:mm a")
            try {
                return timeFormat.format(dateFormat.parse(this)!!)
            } catch (e: ParseException) {
                return null
            }
        }
    }
}