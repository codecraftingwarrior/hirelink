package com.dev.hirelink

import android.app.Application
import android.content.Intent
import androidx.activity.viewModels
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModel
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModelFactory
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.network.auth.AuthRepository
import com.dev.hirelink.network.bankinformation.BankInformationRepository
import com.dev.hirelink.network.company.CompanyRepository
import com.dev.hirelink.network.contractype.ContractTypeRepository
import com.dev.hirelink.network.jobapplication.JobApplicationRepository
import com.dev.hirelink.network.jobcategory.JobCategoryRepository
import com.dev.hirelink.network.joboffer.JobOfferRepository
import com.dev.hirelink.network.paymentinformation.PaymentInformationRepository
import com.dev.hirelink.network.plan.PlanRepository
import com.dev.hirelink.network.profession.ProfessionRepository
import com.dev.hirelink.network.role.RoleRepository
import com.dev.hirelink.network.user.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


// Classe HirelinkApplication héritant de la classe Application
class HirelinkApplication : Application() {

    // Propriété authRepository de type AuthRepository, initialisée avec un objet de type AuthRepository
    // Utilise le mécanisme 'lazy' pour n'instancier l'objet qu'au moment de la première utilisation
    val authRepository: AuthRepository by lazy { AuthRepository(applicationContext) }
    val roleRepository: RoleRepository by lazy { RoleRepository(applicationContext) }
    val userRepository: UserRepository by lazy { UserRepository(applicationContext) }
    val planRepository: PlanRepository by lazy { PlanRepository(applicationContext) }
    val jobOfferRepository: JobOfferRepository by lazy { JobOfferRepository(applicationContext) }
    val companyRepository: CompanyRepository by lazy { CompanyRepository(applicationContext) }
    val professionRepository: ProfessionRepository by lazy { ProfessionRepository(applicationContext) }
    val jobCategoryRepository: JobCategoryRepository by lazy { JobCategoryRepository(applicationContext) }
    val contractTypeRepository: ContractTypeRepository by lazy {
        ContractTypeRepository(
            applicationContext
        )
    }
    val jobApplicationRepository: JobApplicationRepository by lazy {
        JobApplicationRepository(
            applicationContext
        )
    }
    val bankInformationRepository: BankInformationRepository by lazy {
        BankInformationRepository(
            applicationContext
        )
    }
    val paymentInformationRepository: PaymentInformationRepository by lazy {
        PaymentInformationRepository(
            applicationContext
        )
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = SharedPreferenceManager(applicationContext)
        if (sharedPrefs.getJwtToken() != null && sharedPrefs.getJwtToken()!!.isNotEmpty()) {
            val disposable = authRepository
                .fetchCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { applicationUser -> authRepository.emitUser(applicationUser!!) },
                    { error: Throwable -> error.printStackTrace() })
            compositeDisposable.add(disposable)
        }

    }
}