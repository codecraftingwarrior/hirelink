package com.dev.hirelink.modules.auth.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityRegisterBinding
import com.dev.hirelink.enums.PaymentType
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.CandidateRegisterFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.RegisterViewModel
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.ConfirmationCandidateRegisterFragment
import com.dev.hirelink.modules.auth.register.fragments.employerregister.*
import com.dev.hirelink.modules.auth.register.fragments.rolechoose.RoleChooseRegisterFragment
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModel
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModelFactory
import com.dev.hirelink.modules.core.BaseActivity

class RegisterActivity : AppCompatActivity(), RoleChooseRegisterFragment.RoleSelectionListener,
    StepFragment.NextButtonClickListener,
    CandidateRegisterFragment.RegistrationTerminationListener {
    private lateinit var binding: ActivityRegisterBinding
    val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModel.RegisterViewModelFactory(
            this,
            (application as HirelinkApplication).authRepository,
            (application as HirelinkApplication).roleRepository,
            (application as HirelinkApplication).userRepository,
            (application as HirelinkApplication).planRepository,
            (application as HirelinkApplication).paymentInformationRepository,
            (application as HirelinkApplication).bankInformationRepository
        )
    }
    val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (application as HirelinkApplication).authRepository,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        setContentView(binding.root)

        supportActionBar?.hide()
        bindListeners()


        if (savedInstanceState == null)
            inflateRootFragment()

    }

    private fun bindListeners() {
        binding.imageViewBackArrow.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount == 0)
                finish()
            else
                supportFragmentManager.popBackStack()
        }

        binding.rootLinearLayoutRegister.apply { setOnClickListener { hideKeyboard(this) } }

    }

    private fun inflateRootFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.register_activity_fragment_container, RoleChooseRegisterFragment())
        }
    }

    override fun onRoleChosen(role: RoleType) {
        when (role) {
            RoleType.APPLICANT -> {
                replaceFragment("applicant_registration", CandidateRegisterFragment())
            }
            RoleType.EMPLOYER -> replaceFragment(
                "employer_registration",
                EmployerRegisterStep1Fragment(RoleType.EMPLOYER)
            )
            RoleType.INTERIM_AGENCY -> replaceFragment(
                "interim_agency_registration",
                EmployerRegisterStep1Fragment(RoleType.INTERIM_AGENCY)
            )
            else -> Log.e(TAG, "Feature not implemented yet.")
        }
    }

    override fun onNextButtonTouched(step: RegistrationStep, data: Bundle?) {
        when (step) {
            RegistrationStep.STEP_1 -> replaceFragment(
                "otp_code_fill",
                EmployerRegisterStep2Fragment().apply {
                    arguments = data
                }
            )
            RegistrationStep.STEP_2 -> replaceFragment(
                "password_creation",
                EmployerRegisterStep3Fragment().apply {
                    arguments = data
                }
            )
            RegistrationStep.STEP_3 -> replaceFragment(
                "choose_plan",
                EmployerRegisterStep4Fragment().apply {
                    arguments = data
                }
            )
            RegistrationStep.STEP_4 -> replaceFragment(
                "choose_payment",
                EmployerRegisterStep5Fragment().apply {
                    arguments = data
                }
            )
            RegistrationStep.STEP_5 -> showPaymentTypeView(data?.getString("paymentType")!!)
            RegistrationStep.STEP_6_CREDIT_CARD, RegistrationStep.STEP_6_DIRECT_DEBIT -> replaceFragment(
                "start_using_service",
                EmployerRegisterStep7Fragment()
            )
            else -> Log.e(TAG, "feature not implemented yet")
        }
    }

    override fun onRegistrationTerminated(role: RoleType) {
        when (role) {
            RoleType.APPLICANT -> replaceFragment(
                "applicant_registration_terminated",
                ConfirmationCandidateRegisterFragment()
            )
            RoleType.EMPLOYER, RoleType.INTERIM_AGENCY -> startActivity(
                Intent(
                    applicationContext,
                    BaseActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            else -> Log.d(TAG, "Feature not implemented yet.")
        }
    }

    private fun showPaymentTypeView(paymentType: String) {
        when (paymentType) {
            PaymentType.CREDIT_CARD.name -> replaceFragment(
                "credit_card_payment",
                EmployerRegisterStep6CreditCardFragment()
            )
            PaymentType.DIRECT_DEBIT.name -> replaceFragment(
                "direct_debit_payment",
                EmployerRegisterStep6DirectDebitFragment()
            )
        }

    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun replaceFragment(name: String, fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(name)
            replace(R.id.register_activity_fragment_container, fragment)
        }
    }

    companion object {
        val TAG: String = RegisterActivity::class.java.simpleName
    }
}