package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.data.MockDataSource
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep4Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.BasicErrorResponse
import com.dev.hirelink.modules.auth.dto.EmployerChoosePlanRequest
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.auth.register.adapters.PlanItemAdapter
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.RegisterViewModel
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep1Fragment.Companion.NATIONAL_UNIQUE_NUMBER_KEY
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep1Fragment.Companion.USER_EMAIL_KEY
import com.dev.hirelink.modules.auth.register.fragments.employerregister.EmployerRegisterStep1Fragment.Companion.USER_ID_KEY
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class EmployerRegisterStep4Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep4Binding
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var sharedPrefs: SharedPreferenceManager
    private lateinit var planItemAdapter: PlanItemAdapter
    private lateinit var currentUser: ApplicationUser
    private val compositeDisposable = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step4,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        sharedPrefs = SharedPreferenceManager(requireContext())
        registerViewModel = (requireActivity() as RegisterActivity).registerViewModel
        fetchCurrentUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.card_step4,
            R.layout.loading_overlay_centered
        )
        initRecyclerView()

        binding.buttonNext.setOnClickListener { updatePlanForUser() }
    }

    private fun updatePlanForUser() {
        val payload = EmployerChoosePlanRequest(planItemAdapter.selectedPlan.toIRI())
        val userID = currentUser.id

        val disposable = registerViewModel
            .choosePlan(userID!!, payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { user ->
                    Log.d(javaClass.simpleName, user.toString())
                    sharedPrefs.storeRegistrationAchievedStep(RegistrationStep.STEP_4)
                    val bundle = bundleOf(
                        USER_ID_KEY  to arguments?.getInt(USER_ID_KEY),
                        USER_EMAIL_KEY to arguments?.getString(USER_EMAIL_KEY),
                        NATIONAL_UNIQUE_NUMBER_KEY to arguments?.getString(
                            NATIONAL_UNIQUE_NUMBER_KEY
                        )
                    )
                    listener.onNextButtonTouched(RegistrationStep.STEP_4, bundle)
                },
                { error: Throwable -> handleError(error) }
            )

        compositeDisposable.add(disposable)
    }

    private fun initRecyclerView() {
        val plans = MockDataSource().loadPlans()

        val recyclerView = binding.recyclerViewPlan
        planItemAdapter = PlanItemAdapter(requireContext(), plans)
        recyclerView.adapter = planItemAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
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

    private fun fetchCurrentUser() {
        val disposable = registerViewModel
            .authRepository
            .currentUser
            .subscribe { applicationUser -> currentUser = applicationUser!! }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}