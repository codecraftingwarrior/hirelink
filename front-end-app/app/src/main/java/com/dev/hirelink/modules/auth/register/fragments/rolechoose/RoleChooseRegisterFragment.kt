package com.dev.hirelink.modules.auth.register.fragments.rolechoose

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentRoleChooseRegisterBinding
import com.dev.hirelink.enums.RoleType

class RoleChooseRegisterFragment : Fragment() {
    private lateinit var listener: RoleSelectionListener
    private lateinit var selectedRoleType: RoleType

    interface RoleSelectionListener {
        fun onRoleChosen(role: RoleType);
    }

    private lateinit var binding: FragmentRoleChooseRegisterBinding
    private val viewModel: RoleChooseRegisterViewModel by viewModels()

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

        binding.roleChooseViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCandidate.setOnClickListener {
            selectedRoleType = RoleType.APPLICANT
            //listener.onRoleChosen(RoleType.APPLICANT)
        }
        binding.buttonEmployer.setOnClickListener {
            selectedRoleType = RoleType.EMPLOYER
            //listener.onRoleChosen(RoleType.EMPLOYER)
        }
        binding.buttonInterim.setOnClickListener {
            selectedRoleType = RoleType.INTERIM_AGENCY
            // listener.onRoleChosen(RoleType.INTERIM_AGENCY)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as RoleSelectionListener
        } catch (e: java.lang.Exception) {
            throw java.lang.ClassCastException("$context must implement the RoleSelectionListener interface")
        }
    }
}