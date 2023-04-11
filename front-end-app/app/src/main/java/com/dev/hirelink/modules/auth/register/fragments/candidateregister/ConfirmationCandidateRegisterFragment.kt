package com.dev.hirelink.modules.auth.register.fragments.candidateregister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentConfirmationCandidateRegisterBinding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.core.BaseActivity

class ConfirmationCandidateRegisterFragment : Fragment() {
    private lateinit var binding: FragmentConfirmationCandidateRegisterBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_confirmation_candidate_register,
            container,
            false
        );

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonStart.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    BaseActivity::class.java
                )
            )
        }
    }

}