package com.dev.hirelink

import android.app.Application
import com.dev.hirelink.network.auth.AuthRepository
import com.dev.hirelink.network.role.RoleRepository
import com.dev.hirelink.network.user.UserRepository

class HirelinkApplication : Application() {
    val authRepository: AuthRepository by lazy { AuthRepository(applicationContext) }
    val roleRepository: RoleRepository by lazy { RoleRepository(applicationContext) }
    val userRepository: UserRepository by lazy { UserRepository(applicationContext) }
}