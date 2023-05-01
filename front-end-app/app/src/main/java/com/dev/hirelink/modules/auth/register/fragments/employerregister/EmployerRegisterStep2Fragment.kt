package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep2Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.models.BasicErrorResponse
import com.dev.hirelink.modules.auth.dto.OTPVerificationRequest
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

class EmployerRegisterStep2Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep2Binding
    private val currentStep = RegistrationStep.STEP_2
    private lateinit var registerViewModel: RegisterViewModel
    private val compositeDisposable = CompositeDisposable()
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var sharedPrefs: SharedPreferenceManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step2,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        registerViewModel = (requireActivity() as RegisterActivity).registerViewModel
        sharedPrefs = SharedPreferenceManager(requireContext())
        checkStep()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.card_step2,
            R.layout.loading_overlay_centered
        )

        attachTextWatchers()

        bindListeners()
    }

    private fun attachTextWatchers() {
        with(binding) {
            editTextFirstDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 1)
                        editTextSecondDigit.requestFocus()

                    handleTextChange()
                }
            })

            editTextSecondDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 1)
                        editTextThirdDigit.requestFocus()
                    else if (s?.length == 0)
                        editTextFirstDigit.requestFocus()

                    handleTextChange()
                }
            })

            editTextThirdDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 1)
                        editTextFourthDigit.requestFocus()
                    else if (s?.length == 0)
                        editTextSecondDigit.requestFocus()

                    handleTextChange()
                }
            })

            editTextFourthDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 0)
                        editTextThirdDigit.requestFocus()

                    handleTextChange()
                }
            })
        }
    }

    private fun handleTextChange() {
        val firstDigit = binding.editTextFirstDigit.text.toString()
        val secondDigit = binding.editTextSecondDigit.text.toString()
        val thirdDigit = binding.editTextThirdDigit.text.toString()
        val fourthDigit = binding.editTextFourthDigit.text.toString()

        try {
            val digit = "$firstDigit$secondDigit$thirdDigit$fourthDigit".toInt()

            binding.buttonNext.isEnabled = digit in 1000..9999

        } catch (e: NumberFormatException) {
            binding.buttonNext.isEnabled = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindListeners() {
        binding.buttonNext.setOnClickListener { validateOTPDigit() }
    }

    private fun validateOTPDigit() {
        customLoadingOverlay.showLoading()
        val payload = OTPVerificationRequest()
        payload.userID = arguments?.getInt(USER_ID_KEY)
        payload.userEmail = arguments?.getString(USER_EMAIL_KEY)
        payload.otpDigit = listOfNotNull(
            binding.editTextFirstDigit.text.toString(),
            binding.editTextSecondDigit.text.toString(),
            binding.editTextThirdDigit.text.toString(),
            binding.editTextFourthDigit.text.toString()
        ).joinToString("")

        val disposable = registerViewModel
            .verifyDigit(payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { user ->
                    Log.d(javaClass.simpleName, user.toString())
                    sharedPrefs.storeRegistrationAchievedStep(RegistrationStep.STEP_2)
                    val bundle = bundleOf(
                        USER_ID_KEY  to payload.userID,
                        USER_EMAIL_KEY to payload.userEmail,
                        NATIONAL_UNIQUE_NUMBER_KEY to arguments?.getString(
                            NATIONAL_UNIQUE_NUMBER_KEY
                        )
                    )
                    listener.onNextButtonTouched(currentStep, bundle)
                },
                { error: Throwable -> handleError(error) }
            )

        compositeDisposable.add(disposable)
    }

    private fun checkStep() {
        val currentRegistrationStep = sharedPrefs.getCurrentRegistrationStep()
        if (currentRegistrationStep == null || currentRegistrationStep.isEmpty())
            return

        if (currentRegistrationStep == RegistrationStep.STEP_2.name || RegistrationStep.valueOf(
                currentRegistrationStep
            ).number > RegistrationStep.STEP_2.number
        )
            listener.onNextButtonTouched(RegistrationStep.STEP_2)
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