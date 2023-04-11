package com.dev.hirelink.modules.core

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityBaseBinding
import com.dev.hirelink.modules.core.sheets.FilterBottomSheetFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater);
        setContentView(binding.root)

        supportActionBar?.hide()

        setupNavigationBar();
        bindListeners()
    }

    private fun bindListeners() {
        binding.rootConstraintLayout.apply {
            setOnClickListener {
                hideKeyboard(
                    this
                )
            }
        }
        binding.imgBtnFilter.setOnClickListener {
            (FilterBottomSheetFragment()).show(
                supportFragmentManager,
                FilterBottomSheetFragment.TAG
            )
        }

    }

    private fun setupNavigationBar() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_schedule -> {
                    // Respond to navigation item 1 click
                    Log.d(javaClass.simpleName, "schedule item is clicked")
                    true
                }
                R.id.menu_item_candidacy -> {
                    // Respond to navigation item 2 click
                    Log.d(javaClass.simpleName, "candidacy item is clicked")
                    true
                }
                R.id.menu_item_offers -> {
                    Log.d(javaClass.simpleName, "offers item is clicked")
                    true
                }
                R.id.menu_item_notifications -> {
                    Log.d(javaClass.simpleName, "Notifications is clicked")
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.menu.getItem(2).isChecked = true
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}