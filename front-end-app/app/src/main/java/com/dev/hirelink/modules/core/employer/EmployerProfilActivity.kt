package com.dev.hirelink.modules.core.employer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.ActivityEmployerProfilBinding
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.network.auth.AuthRepository
import io.reactivex.disposables.CompositeDisposable

class EmployerProfilActivity : AppCompatActivity() {
    private val compositeDisposable by lazy { CompositeDisposable() }
    private lateinit var binding: ActivityEmployerProfilBinding
    private val sharedPrefs by lazy { SharedPreferenceManager(applicationContext) }
    private val authRepository: AuthRepository by lazy {
        (application as HirelinkApplication).authRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployerProfilBinding.inflate(layoutInflater)
        fetchCurrentUser()
        supportActionBar?.hide()
        setContentView(binding.root)

        bindListeners()
    }

    private fun bindListeners() {
        binding.imgBtnLogout.setOnClickListener {
            sharedPrefs.removeJwtToken()
            val authRepository = (application as HirelinkApplication).authRepository
            authRepository.emitUser(ApplicationUser())
            finish()
        }
        binding.imgBtnArrowLeft.setOnClickListener {
            finish()
        }
    }

    private fun fetchCurrentUser() {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                if (applicationUser?.id != null) {
                    binding.textViewUserFullname.text = applicationUser.company?.name
                }
            }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}