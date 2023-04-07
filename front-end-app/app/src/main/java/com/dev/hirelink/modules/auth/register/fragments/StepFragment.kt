package com.dev.hirelink.modules.auth.register.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.dev.hirelink.enums.RegistrationStep

open class StepFragment : Fragment() {
    protected lateinit var listener: NextButtonClickListener

    interface NextButtonClickListener {
        fun onNextButtonTouched(step: RegistrationStep);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NextButtonClickListener
        } catch (e: java.lang.Exception) {
            throw java.lang.Exception("$context must implements NextButtonClickListener")
        }
    }
}