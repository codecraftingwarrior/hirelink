package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep3Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.BasicErrorResponse
import com.dev.hirelink.modules.auth.dto.EmployerCreatePasswordRequest
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.RegisterViewModel
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep1Fragment.Companion.NATIONAL_UNIQUE_NUMBER_KEY
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep1Fragment.Companion.USER_EMAIL_KEY
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep1Fragment.Companion.USER_ID_KEY
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModel
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class EmployerRegisterStep3Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep3Binding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPrefs: SharedPreferenceManager
    private val compositeDisposable = CompositeDisposable()
    private lateinit var customLoadingOverlay: CustomLoadingOverlay

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step3,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        registerViewModel = (requireActivity() as RegisterActivity).registerViewModel
        authViewModel = (requireActivity() as RegisterActivity).authViewModel
        sharedPrefs = SharedPreferenceManager(requireContext())
        checkStep()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.card_step3,
            R.layout.loading_overlay_centered
        )

        bindListeners();
        attachTextWatchers()
    }

    private fun checkStep() {
        val currentRegistrationStep = sharedPrefs.getCurrentRegistrationStep()
        if (currentRegistrationStep == null || currentRegistrationStep.isEmpty())
            return

        if (currentRegistrationStep == RegistrationStep.STEP_3.name || RegistrationStep.valueOf(
                currentRegistrationStep
            ).number > RegistrationStep.STEP_3.number
        )
            listener.onNextButtonTouched(RegistrationStep.STEP_3)
    }

    private fun bindListeners() {
        binding.buttonNext.setOnClickListener { createPassword() }
    }

    private fun createPassword() {
        customLoadingOverlay.showLoading()
        val userID = arguments?.getInt(USER_ID_KEY)
        val payload = EmployerCreatePasswordRequest()
        payload.email = arguments?.getString(USER_EMAIL_KEY)
        payload.nationalUniqueNumber = arguments?.getString(NATIONAL_UNIQUE_NUMBER_KEY)
        payload.plainPassword = binding.editTextPasswordConfirm.text.toString()

        val disposable = registerViewModel
            .createPasswordForEmployer(userID!!, payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    sharedPrefs.storeJwtToken(user?.token!!)
                    onRegistrationSuccess(user)
                },
                { error: Throwable -> handleError(error) }
            )

        compositeDisposable.add(disposable)

    }

    private fun onRegistrationSuccess(user: ApplicationUser?) {
        Log.d(javaClass.simpleName, user.toString())
        Log.d(javaClass.simpleName, "jwt local storage ${sharedPrefs.getJwtToken()}")
        val disposable = authViewModel
            .fetchCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe({ applicationUser ->
                authViewModel.emitUser(applicationUser)
                val bundle = bundleOf(
                    USER_ID_KEY to arguments?.getInt(USER_ID_KEY),
                    USER_EMAIL_KEY to arguments?.getString(USER_EMAIL_KEY),
                    NATIONAL_UNIQUE_NUMBER_KEY to arguments?.getString(NATIONAL_UNIQUE_NUMBER_KEY)
                )
                sharedPrefs.storeRegistrationAchievedStep(RegistrationStep.STEP_3)
                listener.onNextButtonTouched(RegistrationStep.STEP_3, bundle)

            }, { error: Throwable -> handleError(error) })

        compositeDisposable.add(disposable)
    }

    private fun attachTextWatchers() {
        with(binding) {

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonNext.isEnabled =
                        editTextPassword.text!!.isNotEmpty()
                                && editTextPasswordConfirm.text!!.isNotEmpty()
                                && editTextPassword.text.toString() == editTextPasswordConfirm.text.toString()
                }
            }

            editTextPassword.addTextChangedListener(watcher)
            editTextPasswordConfirm.addTextChangedListener(watcher)
        }

    }

    private fun handleError(error: Throwable) {
        customLoadingOverlay.hideLoading()
        when (error) {
            is HttpException -> {
                try {
                    val errorResponse =
                        HttpExceptionParser.parse(error, BasicErrorResponse::class.java)
                    if (errorResponse.violations?.isEmpty() == false)
                        for (violation in errorResponse.violations)
                            Snackbar.make(
                                binding.buttonNext,
                                violation.message ?: "no message",
                                Snackbar.LENGTH_SHORT
                            ).show()

                    Log.d(javaClass.simpleName, error.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(
                        binding.buttonNext,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
            else -> {
                error.printStackTrace()
                Snackbar.make(
                    binding.buttonNext,
                    getString(R.string.error_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}