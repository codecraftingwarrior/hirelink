package com.dev.hirelink.components

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.net.ParseException
import android.text.format.DateUtils
import java.util.*


object Globals {
    const val API_BASE_URL = "https://e03f-2a04-cec0-1214-a750-5d04-1039-cfef-dcf2.ngrok-free.app/api/";
    const val PLAN_RESOURCE_PREFIX = "/api/plans/"
    const val APPLICATION_USER_RESOURCE_PREFIX = "/api/users/"
    const val BANK_INFORMATIONS_RESOURCE_PREFIX = "/api/bank-informations/"
    const val ROLE_RESOURCE_PREFIX = "/api/roles/"
}

fun toTimeAgo(dateString: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val time: Long = sdf.parse(dateString).time
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
        return ""
    }
}

