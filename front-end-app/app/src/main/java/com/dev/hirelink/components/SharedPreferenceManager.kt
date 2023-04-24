package com.dev.hirelink.components

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "com.dev.hirelink.PREFERENCE_FILE_KEY"
        private const val JWT_TOKEN_KEY = "access_token"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun storeJwtToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(JWT_TOKEN_KEY, token)
        editor.apply()
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString(JWT_TOKEN_KEY, null)
    }
}