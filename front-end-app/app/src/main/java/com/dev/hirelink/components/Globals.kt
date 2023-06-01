package com.dev.hirelink.components

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.Period
import java.util.*


object Globals {
    const val BACKEND_DOMAIN = "https://2c33-46-193-64-92.ngrok-free.app"
    const val API_BASE_URL = "$BACKEND_DOMAIN/api/";
    const val PLAN_RESOURCE_PREFIX = "/api/plans/"
    const val JOB_OFFER_RESOURCE_PREFIX = "/api/job-offers/"
    const val APPLICATION_USER_RESOURCE_PREFIX = "/api/users/"
    const val BANK_INFORMATIONS_RESOURCE_PREFIX = "/api/bank-informations/"
    const val CONTRACT_TYPE_RESOURCE_PREFIX = "/api/contract-types/"
    const val PROFESSION_RESOURCE_PREFIX = "/api/professions/"
    const val JOB_OFFER_CATEGORY_RESOURCE_PREFIX = "/api/job-offer-categories/"
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

fun dateAsString(inputDate: String, pattern: String = "dd/MM/yyyy"): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    val outputFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun getAbsolutePathFromUri(uri: Uri, contentResolver: ContentResolver): String {
    var path: String? = null
    val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
    val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(projection[0])
            path = it.getString(columnIndex)
        }
    }
    return path ?: uri.path ?: ""
}

fun uriToFile(uri: Uri, context: Context, contentResolver: ContentResolver): File? {

    var file: File?

    if (uri.scheme == "file") {
        file = File(uri.path)
    } else {
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val fileName = cursor.getString(columnIndex)
            cursor.close()

            if (fileName != null) {
                val cacheDir = context.externalCacheDir ?: context.cacheDir
                file = File(cacheDir, fileName)
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    if (inputStream != null) {
                        val outputStream = FileOutputStream(file)
                        val buffer = ByteArray(4 * 1024)
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.flush()
                        outputStream.close()
                        inputStream.close()
                    } else {
                        file.delete()
                        file = null
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    file.delete()
                    file = null
                }
            } else {
                file = null
            }
        } else {
            file = null
        }
    }

    return file
}

fun getRootDirPath(context: Context): String {
    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
        val file: File = ContextCompat.getExternalFilesDirs(
            context.applicationContext,
            null
        )[0]
        file.absolutePath
    } else {
        context.applicationContext.filesDir.absolutePath
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateFromNow(date: LocalDate): String {
    val currentDate = LocalDate.now()
    val period = Period.between(currentDate, date)

    return when {
        period.isNegative -> {
            val positivePeriod = period.negated()
            buildPeriodString(positivePeriod, "ago")
        }
        period.isZero -> "today"
        else -> buildPeriodString(period, "in")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun buildPeriodString(period: Period, prefix: String): String {
    val years = period.years
    val months = period.months
    val days = period.days

    return buildString {
        append("$prefix ")

        if (years > 0) append("$years year ")
        if (months > 0) append("$months month ")
        if (days > 0) append("$days day ")
    }
}


