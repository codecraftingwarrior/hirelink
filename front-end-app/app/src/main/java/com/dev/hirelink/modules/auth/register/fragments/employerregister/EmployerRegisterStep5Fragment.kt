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
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep5Binding
import com.dev.hirelink.enums.PaymentType
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.RegisterViewModel
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep4Fragment.Companion.PLAN_NAME_KEY
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep4Fragment.Companion.PLAN_PRICE_KEY
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModel
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import io.reactivex.disposables.CompositeDisposable


class EmployerRegisterStep5Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep5Binding
    private lateinit var selectedPaymentType: PaymentType
    private lateinit var sharedPrefs: SharedPreferenceManager
    private lateinit var currentUser: ApplicationUser
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step5,
            container,
            false
        )
        sharedPrefs = SharedPreferenceManager(requireContext())
        registerViewModel = (requireActivity() as RegisterActivity).registerViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.card_step5,
            R.layout.loading_overlay_centered
        )
        fetchCurrentUser()
        bindListener()
    }

    private fun bindListener() {
        binding.buttonNext.setOnClickListener {
            val bundle = bundleOf(
                "paymentType" to selectedPaymentType.name
            )
            listener.onNextButtonTouched(RegistrationStep.STEP_5, bundle)
        }

        binding.radioBtnCreditCard.setOnClickListener {
            binding.radioBtnDirectDebit.isChecked = false
            selectedPaymentType = PaymentType.CREDIT_CARD
            binding.buttonNext.isEnabled = true
        }

        binding.radioBtnDirectDebit.setOnClickListener {
            binding.radioBtnCreditCard.isChecked = false
            selectedPaymentType = PaymentType.DIRECT_DEBIT
            binding.buttonNext.isEnabled = true
        }
    }

    private fun fetchCurrentUser() {
        customLoadingOverlay.showLoading()
        val disposable = registerViewModel
            .authRepository
            .currentUser
            .subscribe { applicationUser ->
                currentUser = applicationUser!!
                Log.d(javaClass.simpleName, applicationUser.toString())
                binding.textViewChosenPlanName.text = currentUser.plan?.name
                binding.textViewTotalPrice.text = currentUser.plan?.price.toString()
                customLoadingOverlay.hideLoading()
            }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}