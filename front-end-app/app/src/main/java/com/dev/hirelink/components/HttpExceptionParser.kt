package com.dev.hirelink.components

import com.google.gson.Gson
import retrofit2.HttpException

object HttpExceptionParser {

    fun <T> parse(error: HttpException, clazz: Class<T>): T {
        return Gson().fromJson(error.response()?.errorBody()?.string()!!, clazz);
    }
}