package com.dev.hirelink.modules.auth.register.fragments.rolechoose

import android.content.Context
import android.os.Bundle
import android.util.Log
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
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedRole.observe(viewLifecycleOwner) { role ->
            selectedRoleType = when (role) {
                EMPLOYER_ROLE -> RoleType.EMPLOYER
                INTERIM_AGENCY_ROLE -> RoleType.INTERIM_AGENCY
                else -> RoleType.APPLICANT
            }
        }

        binding.buttonNext.setOnClickListener {
            listener.onRoleChosen(selectedRoleType)
            viewModel.eraseRole()
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

    companion object {
        private val APPLICANT_ROLE = "applicant"
        private val EMPLOYER_ROLE = "employer"
        private val INTERIM_AGENCY_ROLE = "interim_agency"
    }
}