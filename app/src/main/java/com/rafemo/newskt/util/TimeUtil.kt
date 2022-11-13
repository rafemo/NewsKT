package com.rafemo.newskt.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun formatDateAgo(date: String): String? {
    try {
        // Api date example: "2022-11-13T11:25:59Z"
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val past = format.parse(date)
        past?.let {
            val duration = Date().time - past.time
            val seconds = TimeUnit.MILLISECONDS.toSeconds(duration)

            if (seconds < 60) {
                return "$seconds s"
            } else {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                return if (minutes < 60) {
                    "$minutes min"
                } else {
                    val hours = TimeUnit.MILLISECONDS.toHours(duration)
                    return if (hours < 24) {
                        "$hours h"
                    } else {
                        val days = TimeUnit.MILLISECONDS.toDays(duration)
                        "$days d"
                    }
                }

            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}