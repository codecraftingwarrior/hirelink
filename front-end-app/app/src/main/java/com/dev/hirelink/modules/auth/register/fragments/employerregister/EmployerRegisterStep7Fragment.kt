package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep7Binding
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.CandidateRegisterFragment

class EmployerRegisterStep7Fragment : Fragment() {
    private lateinit var binding: FragmentEmployerRegisterStep7Binding
    private lateinit var listener: CandidateRegisterFragment.RegistrationTerminationListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step7,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.setOnClickListener { listener.onRegistrationTerminated(RoleType.EMPLOYER) }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as CandidateRegisterFragment.RegistrationTerminationListener
        } catch (e: java.lang.Exception) {
            throw java.lang.Exception("$context must implements NextButtonClickListener")
        }
    }
}