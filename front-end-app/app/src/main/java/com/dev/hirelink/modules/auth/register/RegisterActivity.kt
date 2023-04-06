package com.dev.hirelink.modules.auth.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityRegisterBinding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.CandidateRegisterFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.ConfirmationCandidateRegisterFragment
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep1Fragment
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep2Fragment
import com.dev.hirelink.modules.auth.register.fragments.rolechoose.RoleChooseRegisterFragment

class RegisterActivity : AppCompatActivity(), RoleChooseRegisterFragment.RoleSelectionListener,
    EmployerRegisterStep1Fragment.NextButtonClickListener,
    CandidateRegisterFragment.ApplicantRegistrationTerminationListener {
    private lateinit var binding: ActivityRegisterBinding
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

        binding.rootLinearLayout.apply { setOnClickListener { hideKeyboard(this) } }

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
                EmployerRegisterStep1Fragment()
            )
            else -> Log.e(TAG, "Feature not implemented yet.")
        }
    }

    override fun onNextButtonTouched(step: RegistrationStep) {
        when (step) {
            RegistrationStep.STEP_1 -> createAccount()
            else -> Log.e(TAG, "feature not implemented yet")
        }
    }

    override fun onApplicantRegistrationTerminated() {
        replaceFragment(
            "applicant_registration_terminated",
            ConfirmationCandidateRegisterFragment()
        )
    }

    private fun createAccount() {
        replaceFragment("otp_code_fill", EmployerRegisterStep2Fragment())
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