package com.dev.hirelink.modules.auth.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.dev.hirelink.R
import com.dev.hirelink.modules.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        bindListeners()
    }

    private fun bindListeners() {
        val buttonRegisterText = findViewById<Button>(R.id.button_register_text)
        buttonRegisterText.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        findViewById<ImageButton>(R.id.image_view_back_arrow).setOnClickListener { finish() }
        findViewById<ConstraintLayout>(R.id.root_constraint_layout).apply {
            setOnClickListener {
                hideKeyboard(
                    this
                )
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}