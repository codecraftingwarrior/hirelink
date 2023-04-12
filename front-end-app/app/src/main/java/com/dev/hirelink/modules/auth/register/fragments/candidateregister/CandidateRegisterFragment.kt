package com.dev.hirelink.modules.auth.register.fragments.candidateregister

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentCandidateRegisterBinding
import com.dev.hirelink.enums.RoleType

class CandidateRegisterFragment : Fragment() {
    private lateinit var listener: RegistrationTerminationListener

    interface RegistrationTerminationListener {
        fun onRegistrationTerminated(role: RoleType = RoleType.APPLICANT)
    }

    private lateinit var binding: FragmentCandidateRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_candidate_register,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener { listener.onRegistrationTerminated() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as RegistrationTerminationListener
        } catch (e: java.lang.Exception) {
            throw Exception("$context must implement ApplicantRegistrationTerminationListener")
        }
    }

}