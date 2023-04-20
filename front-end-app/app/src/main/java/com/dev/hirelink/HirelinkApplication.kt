package com.dev.hirelink

import android.app.Application
import com.dev.hirelink.network.auth.AuthRepository

class HirelinkApplication : Application() {
    val authRepository: AuthRepository by lazy { AuthRepository(applicationContext) }
}