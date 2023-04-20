package com.dev.hirelink.modules.auth.register.fragments.candidateregister

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.buttonRegister.isEnabled = false
        binding.buttonRegister.setOnClickListener { listener.onRegistrationTerminated() }
        attachTextWatchers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as RegistrationTerminationListener
        } catch (e: java.lang.Exception) {
            throw Exception("$context must implement ApplicantRegistrationTerminationListener")
        }
    }

    private fun attachTextWatchers() {
        with(binding) {

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonRegister.isEnabled =
                        editTextFirstName.text!!.isNotEmpty()
                                && editTextLastName.text!!.isNotEmpty()
                                && editTextPhoneNumber.text!!.isNotEmpty()
                                && editTextEmail.text!!.isNotEmpty()
                                && editTextPassword.text!!.isNotEmpty()
                                && editTextPasswordConfirm.text!!.isNotEmpty()
                }
            }

            editTextFirstName.addTextChangedListener(watcher)
            editTextLastName.addTextChangedListener(watcher)
            editTextPhoneNumber.addTextChangedListener(watcher)
            editTextEmail.addTextChangedListener(watcher)
            editTextPassword.addTextChangedListener(watcher)
            editTextPasswordConfirm.addTextChangedListener(watcher)
        }
    }

}