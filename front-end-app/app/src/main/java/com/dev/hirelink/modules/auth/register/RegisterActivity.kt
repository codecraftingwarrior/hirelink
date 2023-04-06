package com.dev.hirelink.modules.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.fragment.app.commit
import com.dev.hirelink.R
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.CandidateRegisterFragment
import com.dev.hirelink.modules.auth.register.fragments.rolechoose.RoleChooseRegisterFragment

class RegisterActivity : AppCompatActivity(), RoleChooseRegisterFragment.RoleSelectionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()
        bindListeners()

        if (savedInstanceState == null)
            inflateRootFragment()
    }

    private fun bindListeners() {
        findViewById<ImageButton>(R.id.image_view_back_arrow).setOnClickListener {
            if (supportFragmentManager.backStackEntryCount == 0)
                finish()
            else
                supportFragmentManager.popBackStack()
        }
    }

    private fun inflateRootFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.register_activity_fragment_container, RoleChooseRegisterFragment())
        }
    }

    override fun onRoleChosen(role: RoleType) {
        when (role) {
            RoleType.APPLICANT -> supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("applicant_registration")
                replace(R.id.register_activity_fragment_container, CandidateRegisterFragment())
            }
            else -> Log.e(TAG, "Feature not implemented yet.")
        }
    }

    companion object {
        val TAG: String = RegisterActivity::class.java.simpleName
    }
}