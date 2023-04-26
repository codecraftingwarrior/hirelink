package com.dev.hirelink.modules.auth.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.ActivityLoginBinding
import com.dev.hirelink.modules.auth.dto.Credentials
import com.dev.hirelink.modules.auth.dto.LoginErrorResponse
import com.dev.hirelink.modules.auth.dto.LoginResponse
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModel
import com.dev.hirelink.modules.auth.viewmodel.AuthViewModelFactory
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.BaseActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var sharedPrefs: SharedPreferenceManager
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (application as HirelinkApplication).authRepository,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setContentView(binding.root)

        customLoadingOverlay = CustomLoadingOverlay(this, R.id.root_constraint_layout_login, R.layout.loading_overlay)
        sharedPrefs = SharedPreferenceManager(this)

        supportActionBar?.hide()

        binding.buttonLogin.isEnabled = false

        bindListeners()
        bindTextWatchers()
    }

    private fun bindTextWatchers() {
        with(binding) {

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonLogin.isEnabled =
                        editTextUsername.text!!.isNotEmpty() && editTextPassword.text!!.isNotEmpty()
                }
            }

            editTextUsername.addTextChangedListener(watcher)
            editTextPassword.addTextChangedListener(watcher)
        }

    }

    private fun bindListeners() {
        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        binding.imageViewBackArrow.setOnClickListener { finish() }
        binding.rootConstraintLayoutLogin.apply {
            setOnClickListener {
                hideKeyboard(
                    this
                )
            }
        }

        binding
            .buttonLogin
            .setOnClickListener { doLogin() }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun doLogin() {
        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()

        customLoadingOverlay.showLoading()
        val disposable = authViewModel
            .login(Credentials(username, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { loginResponse -> onLoginSuccess(loginResponse) },
                { error: Throwable -> handleError(error) }
            )

        compositeDisposable.add(disposable)
    }

    private fun onLoginSuccess(loginResponse: LoginResponse?) {
        Log.d(javaClass.simpleName, loginResponse.toString())
        sharedPrefs.storeJwtToken(loginResponse?.token!!)
        customLoadingOverlay.showLoading()
        val disposable = authViewModel
            .fetchCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe({ applicationUser ->
                authViewModel.emitUser(applicationUser)

                val intent = Intent(this, BaseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }, { error: Throwable -> handleError(error) }
            )

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is HttpException -> {
                try {
                    val loginErrorResponse = HttpExceptionParser.parse(error, LoginErrorResponse::class.java)
                    Snackbar.make(binding.buttonLogin, loginErrorResponse.message, Snackbar.LENGTH_SHORT).show()
                } catch(e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(
                        binding.buttonLogin,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
            else -> {
                error.printStackTrace()
                Snackbar.make(
                    binding.buttonLogin,
                    getString(R.string.error_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}