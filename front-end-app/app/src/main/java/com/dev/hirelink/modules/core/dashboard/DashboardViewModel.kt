package com.dev.hirelink.modules.core.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.network.dashboard.DashboardRepository
import com.dev.hirelink.network.user.UserRepository

class DashboardViewModel(
    val repository: DashboardRepository,
    val userRepository: UserRepository
) : ViewModel() {

    fun findStats() = repository.fetchStats()

    fun findAllUsers(page: Int? = null, role: String? = null) = userRepository.findAll(page, role)

    class DashboardViewModelFactory(
        private val context: Context,
        private val dashboardRepository: DashboardRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(dashboardRepository, userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}