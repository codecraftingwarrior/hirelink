package com.dev.hirelink.modules.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.commit
import com.dev.hirelink.R
import com.dev.hirelink.modules.auth.register.fragments.RoleChooseRegisterFragment

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()
        bindListeners()

        if (savedInstanceState == null)
            inflateRootFragment()
    }

    private fun bindListeners() {
        findViewById<ImageButton>(R.id.image_view_back_arrow).setOnClickListener { finish() }
    }

    private fun inflateRootFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.register_activity_fragment_container, RoleChooseRegisterFragment())
        }
    }
}