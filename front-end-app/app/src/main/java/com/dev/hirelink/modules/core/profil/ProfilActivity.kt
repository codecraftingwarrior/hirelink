package com.dev.hirelink.modules.core.profil

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.ActivityProfilBinding
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.network.auth.AuthRepository
import io.reactivex.disposables.CompositeDisposable

class ProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding
    private lateinit var sharedPrefs: SharedPreferenceManager
    private lateinit var authRepository: AuthRepository
    private val compositeDisposable = CompositeDisposable()
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        authRepository = (application as HirelinkApplication).authRepository

        binding.imgBtnArrowLeft.setOnClickListener { finish() }
        sharedPrefs = SharedPreferenceManager(applicationContext)
        bindListeners()
        fetchCurrentUser()
    }

    private fun bindListeners() {
        binding.imgBtnLogout.setOnClickListener {
            sharedPrefs.removeJwtToken()
            val authRepository = (application as HirelinkApplication).authRepository
            authRepository.emitUser(ApplicationUser())
            finish()
        }
    }

    private fun fetchCurrentUser() {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                Log.d(
                    javaClass.simpleName,
                    applicationUser.toString()
                )
                isLoggedIn = applicationUser?.id != null
                if (isLoggedIn) {
                    binding.textViewUserFullname.text = when(RoleType.valueOf(applicationUser?.role?.name!!)) {
                        RoleType.APPLICANT -> applicationUser.fullname()
                        RoleType.EMPLOYER, RoleType.INTERIM_AGENCY -> applicationUser.company?.name
                        else -> ""
                    }
                }
            }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}