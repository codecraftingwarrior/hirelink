package com.dev.hirelink.modules.auth.register.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoleChooseRegisterViewModel : ViewModel() {
    private val _selectedRole: MutableLiveData<String> by lazy { MutableLiveData("") }
    val selectedRole: LiveData<String>
        get() = _selectedRole


    fun onRoleSelected(role: String) {
        Log.d(javaClass.simpleName, role)
        //TODO: update the selected role color background & display the next button
    }
}