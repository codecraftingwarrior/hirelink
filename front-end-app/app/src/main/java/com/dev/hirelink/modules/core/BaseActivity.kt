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
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityBaseBinding
import com.dev.hirelink.modules.core.offers.fragment.JobOfferListFragment
import com.dev.hirelink.modules.core.sheets.FilterBottomSheetFragment
import com.dev.hirelink.network.auth.AuthRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import io.reactivex.disposables.CompositeDisposable

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private val authRepository: AuthRepository by lazy { (application as HirelinkApplication).authRepository }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater);
        setContentView(binding.root)

        supportActionBar?.hide()

        setupNavigationBar();
        bindListeners()

        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                Log.d(
                    javaClass.simpleName,
                    applicationUser.toString()
                )
            }

        compositeDisposable.add(disposable)
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
                    true
                }
                R.id.menu_item_candidacy -> {
                    // Respond to navigation item 2 click
                    Log.d(javaClass.simpleName, "candidacy item is clicked")
                    true
                }
                R.id.menu_item_offers -> {
                    replaceFragment(JobOfferListFragment())
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
        replaceFragment(JobOfferListFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.register_activity_fragment_container, fragment)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}