package com.dev.hirelink.modules.auth.register.fragments.candidateregister

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.ApplicationUserRequest
import com.dev.hirelink.network.auth.AuthRepository
import com.dev.hirelink.network.role.RoleRepository

class CandidateRegisterViewModel(
   private val authRepository: AuthRepository,
    val roleRepository: RoleRepository
) : ViewModel() {
    private val _user: ApplicationUser = ApplicationUser()
    val user: ApplicationUser
        get() = _user

    fun register(user: ApplicationUserRequest) = authRepository.register(user)

    class CandidateRegisterViewModelFactory(
        private val context: Context,
        private val authRepository: AuthRepository,
        private val roleRepository: RoleRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CandidateRegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CandidateRegisterViewModel(authRepository, roleRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}