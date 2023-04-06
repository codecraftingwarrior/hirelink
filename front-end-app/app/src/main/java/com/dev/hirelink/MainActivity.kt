package com.dev.hirelink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.dev.hirelink.modules.auth.login.LoginActivity
import com.dev.hirelink.modules.auth.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        bindListeners();
    }

    private fun bindListeners() {
        val registerButton = findViewById<AppCompatButton>(R.id.register_btn)
        registerButton.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    RegisterActivity::class.java
                )
            )
        }

        val loginButton = findViewById<AppCompatButton>(R.id.login_btn)
        loginButton.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    LoginActivity::class.java
                )
            )
        }
    }
}