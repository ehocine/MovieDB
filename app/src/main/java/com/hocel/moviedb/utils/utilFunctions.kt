package com.hocel.moviedb.utils

fun convertMinutes(minutes: Int): String {

    val numberOfHours = (minutes / 60)
    val numberOfMinutes = (minutes % 60)

    val hoursText = when {
        numberOfHours < 10 -> "0$numberOfHours"
        numberOfHours == 0 -> "00"
        else -> numberOfHours
    }

    val minutesText = when {
        numberOfMinutes < 10 -> "0$numberOfMinutes"
        numberOfMinutes == 0 -> "00"
        else -> numberOfMinutes
    }
    return "${hoursText}h$minutesText"
}