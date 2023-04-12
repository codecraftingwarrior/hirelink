package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep6CreditCardBinding
import com.dev.hirelink.enums.PaymentType
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment

class EmployerRegisterStep6CreditCardFragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep6CreditCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step6_credit_card,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener { storePaymentInformations() }
    }

    private fun storePaymentInformations() {
        listener.onNextButtonTouched(RegistrationStep.STEP_6_CREDIT_CARD)
    }

    companion object {
        val TAG = EmployerRegisterStep6CreditCardFragment::class.java.simpleName
    }
}