package com.dev.hirelink.modules.core.profil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityProfilSettingsBinding
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.network.auth.AuthRepository
import io.reactivex.disposables.CompositeDisposable

class ProfilSettingsActivity : AppCompatActivity() {
    private val authRepository: AuthRepository by lazy { (application as HirelinkApplication).authRepository }
    private lateinit var currentUser: ApplicationUser
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var binding: ActivityProfilSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        bindListeners()
        fetchCurrentUser()
    }

    private fun bindListeners() {
        binding.imgBtnArrowLeft.setOnClickListener { finish() }
    }

    private fun fetchCurrentUser() {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                currentUser = applicationUser ?: ApplicationUser()
                initLayout()
            }

        compositeDisposable.add(disposable)
    }

    private fun initLayout() {
        with(binding) {
            textViewFullnameProfil.text = listOfNotNull(currentUser.firstName, currentUser.lastName).joinToString(" ")
            editTextFirstName.setText(currentUser.firstName.toString())
            editTextFieldLastName.setText(currentUser.lastName.toString())
            editTextFieldEmail.setText(currentUser.email.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}