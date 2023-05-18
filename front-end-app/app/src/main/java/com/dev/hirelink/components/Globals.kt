package com.dev.hirelink.components

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.net.ParseException
import android.net.Uri
import android.provider.MediaStore
import android.text.format.DateUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


object Globals {
    const val API_BASE_URL = "https://edd0-46-193-64-92.ngrok-free.app/api/";
    const val PLAN_RESOURCE_PREFIX = "/api/plans/"
    const val JOB_OFFER_RESOURCE_PREFIX = "/api/job-offers/"
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


