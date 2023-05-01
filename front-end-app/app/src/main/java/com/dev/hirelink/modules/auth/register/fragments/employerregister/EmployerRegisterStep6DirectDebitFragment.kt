package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep6DirectDebitBinding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.dto.BasicErrorResponse
import com.dev.hirelink.dto.BankInformationDTO
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.RegisterViewModel
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class EmployerRegisterStep6DirectDebitFragment : StepFragment() {

    private lateinit var binding: FragmentEmployerRegisterStep6DirectDebitBinding
    private lateinit var sharedPrefs: SharedPreferenceManager
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var currentUser: ApplicationUser
    private val compositeDisposable = CompositeDisposable()
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
        sharedPrefs = SharedPreferenceManager(requireContext())
        registerViewModel = (requireActivity() as RegisterActivity).registerViewModel
        fetchCurrentUser()
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.card_step6_dd,
            R.layout.loading_overlay_centered
        )
        binding.buttonNext.setOnClickListener { storePaymentInformations() }
        attachTextWatchers()
    }

    private fun storePaymentInformations() {
        customLoadingOverlay.showLoading()

        val payload = BankInformationDTO()
        payload.iban = binding.editTextIban.text.toString()
        payload.bic = binding.editTextBic.text.toString()
        payload.owner = currentUser.toIRI()

        val disposable = registerViewModel
            .createBankInformation(payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { paymentInformation ->
                    Log.d(javaClass.simpleName, paymentInformation.toString())
                    sharedPrefs.clearRegistrationStep()
                    listener.onNextButtonTouched(RegistrationStep.STEP_6_DIRECT_DEBIT)
                },
                { error: Throwable -> handleError(error) }
            )

        compositeDisposable.add(disposable)
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
                                "${violation.propertyPath}: ${violation.message}" ?: "no message",
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


    private fun attachTextWatchers() {

        with(binding) {

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonNext.isEnabled =
                        editTextAccountOwner.text!!.isNotEmpty()
                                && editTextIban.text!!.isNotEmpty()
                                && editTextBic.text!!.isNotEmpty()

                }
            }

            editTextAccountOwner.addTextChangedListener(watcher)
            editTextIban.addTextChangedListener(watcher)
            editTextBic.addTextChangedListener(watcher)
        }
    }

    private fun fetchCurrentUser() {
        val disposable = registerViewModel
            .authRepository
            .currentUser
            .subscribe { applicationUser ->
                currentUser = applicationUser!!
                Log.d(javaClass.simpleName, applicationUser.toString())
            }

        compositeDisposable.add(disposable)
    }
}