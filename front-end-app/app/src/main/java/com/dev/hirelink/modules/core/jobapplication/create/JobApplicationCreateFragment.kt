package com.dev.hirelink.modules.core.jobapplication.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentBottomSheetCandidacyNewBinding
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.core.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class JobApplicationCreateFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCandidacyNewBinding
    private lateinit var currentUser: ApplicationUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_sheet_candidacy_new,
            container,
            false
        )

        currentUser = (requireActivity() as BaseActivity).currentUser
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewCandidacyNewInitials.text = listOfNotNull(
            currentUser.firstName?.get(0),
            currentUser.lastName?.get(0)
        ).joinToString("")
        binding.textViewCandidacyFirstName.text = currentUser.firstName
        binding.textViewCandidacyLastName.text = currentUser.lastName
    }

    companion object {
        val TAG = JobApplicationCreateFragment::class.java.simpleName
    }
}