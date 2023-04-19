package com.dev.hirelink.modules.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.dto.Credentials
import com.dev.hirelink.network.auth.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    fun login(credentials: Credentials) = repository.login(credentials)

    fun fetchCurrentUser() = repository.fetchCurrentUser()

    fun emitUser(user: ApplicationUser?) = repository.emitUser(user!!)
}