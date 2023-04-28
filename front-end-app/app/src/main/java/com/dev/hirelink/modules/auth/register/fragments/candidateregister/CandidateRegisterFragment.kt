package com.dev.hirelink.modules.auth.register.fragments.candidateregister

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.FragmentCandidateRegisterBinding
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.*
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModel
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class CandidateRegisterFragment : Fragment() {
    private lateinit var listener: RegistrationTerminationListener
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPrefs: SharedPreferenceManager
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private val applicantRole =
        Role(id = 1, code = RoleType.APPLICANT.code, name = RoleType.APPLICANT.name)

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
        registerViewModel =
            (requireActivity() as RegisterActivity).registerViewModel
        authViewModel = (requireActivity() as RegisterActivity).authViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        sharedPrefs = SharedPreferenceManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.isEnabled = false
        binding.buttonRegister.setOnClickListener { register() }

        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.root_constraint_layout_candidate_register,
            R.layout.loading_overlay_centered
        )

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
                                && editTextPassword.text.toString() == editTextPasswordConfirm.text.toString()
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

    private fun register() {
        customLoadingOverlay.showLoading()
        val applicationUser = ApplicationUserRequest(
            firstName = binding.editTextFirstName.text.toString(),
            lastName = binding.editTextLastName.text.toString(),
            phoneNumber = binding.editTextPhoneNumber.text.toString(),
            email = binding.editTextEmail.text.toString(),
            plainPassword = binding.editTextPassword.text.toString(),
            role = registerViewModel.roleRepository.toIRI(this.applicantRole)
        )

        val disposable = registerViewModel
            .register(applicationUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user -> onRegistrationSuccess(user) },
                { error: Throwable -> handleError(error) }
            )
        compositeDisposable.add(disposable)

    }

    private fun onRegistrationSuccess(user: ApplicationUser?) {
        Log.d(javaClass.simpleName, user.toString())
        sharedPrefs.storeJwtToken(user?.token!!)
        val disposable = authViewModel
            .fetchCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe({ applicationUser ->
                authViewModel.emitUser(applicationUser)
                listener.onRegistrationTerminated()
            }, { error: Throwable -> handleError(error) })

        compositeDisposable.add(disposable)
    }

    private fun handleError(error: Throwable) {
        customLoadingOverlay.hideLoading()
        when (error) {
            is HttpException -> {
                try {
                    val errorResponse = HttpExceptionParser.parse(error, BasicErrorResponse::class.java)
                    if(errorResponse.violations?.isEmpty() == false)
                        for (violation in errorResponse.violations)
                            Snackbar.make(binding.buttonRegister, violation.message ?: "no message", Snackbar.LENGTH_SHORT).show()

                    Log.d(javaClass.simpleName, error.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(
                        binding.buttonRegister,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
            else -> {
                error.printStackTrace()
                Snackbar.make(
                    binding.buttonRegister,
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