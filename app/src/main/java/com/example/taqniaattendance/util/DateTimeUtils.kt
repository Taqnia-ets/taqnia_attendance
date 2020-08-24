package com.example.taqniaattendance.util

import android.util.Log
import com.example.taqniaattendance.util.TimeUtils.GMTDateTimeFormatter
import com.example.taqniaattendance.util.TimeUtils.IsoDateTimeFormatter
import com.example.taqniaattendance.util.TimeUtils.MMMDDYYYYFormatter
import com.example.taqniaattendance.util.TimeUtils.formalDateFormatter
import com.example.taqniaattendance.util.TimeUtils.userDateWithoutTime
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.format.DateTimeParseException
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    private const val ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    private const val GMT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    private const val USER_DATE_FORMAT = "MM/dd/yyyy hh:mm aa"
    private const val USER_DATE_WITHOUT_TIME_FORMAT = "dd/MM/yyyy"
    private const val MMMDDYYYY_FORMAT = "MMM d, yyyy"

    internal val IsoDateTimeFormatter by lazy {
        SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.ENGLISH)
    }

    internal val GMTDateTimeFormatter by lazy {
        SimpleDateFormat(GMT_DATE_TIME_FORMAT, Locale.ENGLISH)
    }

    internal val formalDateFormatter by lazy {
        SimpleDateFormat(USER_DATE_FORMAT, Locale.ENGLISH)
    }

    internal val userDateWithoutTime by lazy {
        SimpleDateFormat(USER_DATE_WITHOUT_TIME_FORMAT, Locale.ENGLISH)
    }

    internal val MMMDDYYYYFormatter by lazy {
        SimpleDateFormat(MMMDDYYYY_FORMAT, Locale.ENGLISH)
    }
}

fun String?.toMilles() = try {
    IsoDateTimeFormatter.parse(this).time
} catch (e: Exception){
    0L
}

fun String?.toMMMDDYYYYFormat() = try {
    val date =  userDateWithoutTime.parse(this)
    MMMDDYYYYFormatter.format(date)
} catch (e: Exception) {
    this
}

fun String?.fromMMMDDYYYYToDate() : Date? = try {
    userDateWithoutTime.parse(this)
} catch (e: Exception) {
    Log.getStackTraceString(e)
    null
}

fun Long.toDateFormat() = try {
    IsoDateTimeFormatter.format(Date(this))
} catch (e: Exception){
    ""
}

fun Long.toMinutes() = TimeUnit.SECONDS.toMinutes(this).toString()

fun Long.toTimeFormat() = try {
    String.format(
        Locale.ENGLISH,
        "%02d:%02d:%02d",
        (this / 3600000).toInt(), // hours
        (this / 60000 % 60).toInt(), // minutes
        (this/ 1000 % 60).toInt() // seconds
    )
} catch (e: Exception){
    ""
}


fun String?.toDateFormat(): String = try {
    val dateIso =  IsoDateTimeFormatter.parse(this)
    formalDateFormatter.format(dateIso)
} catch (e: Exception){
    ""
}

fun String?.fromGMTtoDateFormat(): String = try {
    GMTDateTimeFormatter.timeZone = TimeZone.getTimeZone("GMT")
    val dateIso =  GMTDateTimeFormatter.parse(this)
    formalDateFormatter.timeZone = TimeZone.getDefault()
    formalDateFormatter.format(dateIso)
} catch (e: Exception){
    ""
}

fun String.fromMillToUserFormat() : String = formalDateFormatter.format(Date(this.toLong()))

