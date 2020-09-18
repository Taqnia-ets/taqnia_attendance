package com.example.taqniaattendance.util

import android.content.Context
import java.lang.NumberFormatException

/**
 * convert string value to Int in case there is error in number format it will return zero
 */
fun String?.toIntOrZero(): Int {
    return try {
        this?.toInt() ?: 0
    }catch (e : NumberFormatException) {
        0
    }
}

fun Int?.isNullOrZero() : Boolean {
    return (this == null || this == 0)
}

