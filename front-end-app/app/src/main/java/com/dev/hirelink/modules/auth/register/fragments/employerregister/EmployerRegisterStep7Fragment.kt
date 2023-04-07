package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep7Binding
import com.dev.hirelink.modules.auth.register.fragments.StepFragment

class EmployerRegisterStep7Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep7Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step1,
            container,
            false
        )

        return binding.root
    }
}