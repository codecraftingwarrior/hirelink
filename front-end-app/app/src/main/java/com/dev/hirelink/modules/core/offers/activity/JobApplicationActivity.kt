package com.dev.hirelink.modules.core.offers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityJobApplicationBinding

class JobApplicationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobApplicationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}