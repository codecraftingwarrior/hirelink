package com.dev.hirelink.modules.auth.register.fragments.candidateregister

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.models.*
import com.dev.hirelink.modules.auth.dto.BankInformationDTO
import com.dev.hirelink.modules.auth.dto.EmployerChoosePlanRequest
import com.dev.hirelink.modules.auth.dto.EmployerCreatePasswordRequest
import com.dev.hirelink.modules.auth.dto.OTPVerificationRequest
import com.dev.hirelink.network.auth.AuthRepository
import com.dev.hirelink.network.bankinformation.BankInformationRepository
import com.dev.hirelink.network.paymentinformation.PaymentInformationRepository
import com.dev.hirelink.network.plan.PlanRepository
import com.dev.hirelink.network.role.RoleRepository
import com.dev.hirelink.network.user.UserRepository

class RegisterViewModel(
    val authRepository: AuthRepository,
    val roleRepository: RoleRepository,
    val userRepository: UserRepository,
    val planRepository: PlanRepository,
    val paymentInformationRepository: PaymentInformationRepository,
    val bankInformationRepository: BankInformationRepository
) : ViewModel() {
    private val _user: ApplicationUser = ApplicationUser()
    val user: ApplicationUser
        get() = _user

    fun register(user: ApplicationUserRequest) = authRepository.register(user)

    fun verifyDigit(otpVerificationRequest: OTPVerificationRequest) =
        authRepository.verifyOTPDigit(otpVerificationRequest)

    fun createPasswordForEmployer(id: Int, payload: EmployerCreatePasswordRequest) =
        authRepository.createPassword(id, payload)

    fun choosePlan(id: Int, payload: EmployerChoosePlanRequest) =
        userRepository.choosePlan(id, payload)

    fun createPaymentInformation(payload: PaymentInformationDTO) =
        paymentInformationRepository.store(payload)

    fun createBankInformation(payload: BankInformationDTO) =
        bankInformationRepository.store(payload)

    class RegisterViewModelFactory(
        private val context: Context,
        private val authRepository: AuthRepository,
        private val roleRepository: RoleRepository,
        private val userRepository: UserRepository,
        private val planRepository: PlanRepository,
        private val paymentInformationRepository: PaymentInformationRepository,
        private val bankInformationRepository: BankInformationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(
                    authRepository,
                    roleRepository,
                    userRepository,
                    planRepository,
                    paymentInformationRepository,
                    bankInformationRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}