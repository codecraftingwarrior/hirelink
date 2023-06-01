package com.dev.hirelink.modules.core.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.network.dashboard.DashboardRepository

class DashboardViewModel(
    val repository: DashboardRepository
): ViewModel() {

    fun findStats() = repository.fetchStats()

    class DashboardViewModelFactory(
        private val context: Context,
        private val dashboardRepository: DashboardRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(dashboardRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}