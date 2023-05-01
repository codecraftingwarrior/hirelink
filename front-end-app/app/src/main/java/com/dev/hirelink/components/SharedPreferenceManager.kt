package com.dev.hirelink.components

import android.content.Context
import android.content.SharedPreferences
import com.dev.hirelink.enums.RegistrationStep

class SharedPreferenceManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "com.dev.hirelink.PREFERENCE_FILE_KEY"
        const val JWT_TOKEN_KEY = "access_token"
        private const val REGISTRATION_STEP_KEY = "registration_step"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun storeJwtToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(JWT_TOKEN_KEY, token)
        editor.apply()
    }

    fun hasKey(key: String) = sharedPreferences.contains(key)

    fun storeRegistrationAchievedStep(step: RegistrationStep) {
        val editor = sharedPreferences.edit()
        editor.putString(REGISTRATION_STEP_KEY, step.name)
        editor.apply()
    }

    fun getCurrentRegistrationStep(): String? =
        sharedPreferences.getString(REGISTRATION_STEP_KEY, null)

    fun clearRegistrationStep() {
        val editor = sharedPreferences.edit()
        editor.remove(REGISTRATION_STEP_KEY)
        editor.apply()
    }

    fun removeJwtToken() {
        val editor = sharedPreferences.edit()
        editor.remove(JWT_TOKEN_KEY)
        editor.apply()
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString(JWT_TOKEN_KEY, null)
    }
}