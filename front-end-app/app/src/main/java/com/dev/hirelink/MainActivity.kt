package com.dev.hirelink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.components.SharedPreferenceManager.Companion.JWT_TOKEN_KEY
import com.dev.hirelink.modules.auth.login.LoginActivity
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.core.BaseActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPrefs: SharedPreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs = SharedPreferenceManager(applicationContext)

        if(sharedPrefs.hasKey(JWT_TOKEN_KEY)) {
            startActivity(Intent(this, BaseActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_main)

            supportActionBar?.hide()

            bindListeners();
        }
    }

    private fun bindListeners() {
        val registerButton = findViewById<AppCompatButton>(R.id.button_register)
        registerButton.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    RegisterActivity::class.java
                )
            )
        }

        val loginButton = findViewById<AppCompatButton>(R.id.button_login)
        loginButton.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    LoginActivity::class.java
                )
            )
        }

        val viewOffersButton = findViewById<Button>(R.id.button_view_offers)
        viewOffersButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    BaseActivity::class.java
                )
            )
        }

    }
}