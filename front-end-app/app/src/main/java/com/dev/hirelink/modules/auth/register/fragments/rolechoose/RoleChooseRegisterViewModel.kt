package com.dev.hirelink.modules.auth.register.fragments.rolechoose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoleChooseRegisterViewModel : ViewModel() {
    private val _selectedRole: MutableLiveData<String> by lazy { MutableLiveData("") }
    val selectedRole: LiveData<String>
        get() = _selectedRole


    fun onRoleSelected(role: String) {
        _selectedRole.value = role;
    }

    fun eraseRole() {
        _selectedRole.value = ""
    }
}