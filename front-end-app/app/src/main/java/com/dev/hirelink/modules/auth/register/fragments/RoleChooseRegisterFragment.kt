package com.dev.hirelink.modules.auth.register.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentRoleChooseRegisterBinding

class RoleChooseRegisterFragment : Fragment() {
    private lateinit var binding: FragmentRoleChooseRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_role_choose_register,
            container,
            false
        )

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO : set event listener here ie:  binding.buttonCandidate.setOnClickListener {  }

    }
}