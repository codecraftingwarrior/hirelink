package com.dev.hirelink.modules.core.profil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityProfilBinding

class ProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.imgBtnArrowLeft.setOnClickListener { finish() }
    }
}