package com.hocel.moviedb.utils

import android.app.Activity
import android.content.Context
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

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

fun showStatusBar(context: Context) {
    val window = (context as? Activity)?.window
    val windowInsetsController = window?.let {
        window.decorView.let { it1 ->
            WindowCompat.getInsetsController(it,
                it1
            )
        }
    }
    windowInsetsController?.show(WindowInsetsCompat.Type.statusBars())
}