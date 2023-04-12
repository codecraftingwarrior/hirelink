package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep6DirectDebitBinding
import com.dev.hirelink.enums.PaymentType
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment


class EmployerRegisterStep6DirectDebitFragment : StepFragment() {

    private lateinit var binding: FragmentEmployerRegisterStep6DirectDebitBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step6_direct_debit,
            container,
            false
        )
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = bundleOf("paymentType" to PaymentType.DIRECT_DEBIT.name)
        binding.buttonNext.setOnClickListener {
            listener.onNextButtonTouched(
                RegistrationStep.STEP_6_DIRECT_DEBIT,
                bundle
            )
        }
    }
}