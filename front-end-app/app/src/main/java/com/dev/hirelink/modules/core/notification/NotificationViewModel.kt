package com.dev.hirelink.modules.core.notification

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.network.notifications.NotificationRepository

class NotificationViewModel(
    val notificationRepository: NotificationRepository
): ViewModel() {

    fun fetchNotifications(page: Int? = null, user: Int? = null) = notificationRepository.findAll(page, user)

    fun getUnreadNotificationCount() = notificationRepository.getUnreadNotificationCount()

    fun markAllAsRead() = notificationRepository.markAllAsRead()

    class NotificationViewModelFactory(
        private val context: Context,
        private val repository: NotificationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}