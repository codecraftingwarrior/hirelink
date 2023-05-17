package com.dev.hirelink.modules.core.jobapplication.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentBottomSheetCandidacyNewBinding
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.core.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File

class JobApplicationCreateFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCandidacyNewBinding
    private lateinit var currentUser: ApplicationUser
    private val FILE_CV_CHOOSE_REQUEST_CODE = 1
    private lateinit var filePickerActivityResult: ActivityResultLauncher<Intent>
    private lateinit var chosenPDFFile: File

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
        filePickerActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data

                    handlePdfSelection(data)
                }
            }
        return binding.root
    }

    @SuppressLint("Range")
    private fun handlePdfSelection(data: Intent?) {
        val uri: Uri = data?.data!!
        val uriString: String = uri.toString()
        var pdfName: String? = null
        chosenPDFFile = uri.path?.let { File(it) }!!

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = requireContext()
                    .contentResolver
                    .query(uri, null, null, null, null)

                if (cursor != null && cursor.moveToFirst()) {
                    pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    binding.textViewCvChooserHintCandidacyNew.text = pdfName
                }
            } finally {
                cursor?.close()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewCandidacyNewInitials.text = listOfNotNull(
            currentUser.firstName?.get(0),
            currentUser.lastName?.get(0)
        ).joinToString("")
        binding.textViewCandidacyFirstName.text = currentUser.firstName
        binding.textViewCandidacyLastName.text = currentUser.lastName

        bindListeners()
    }

    private fun bindListeners() {
        binding.linearLayoutCvChoose.setOnClickListener {
            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
            pdfIntent.type = "application/pdf"
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)

            filePickerActivityResult.launch(pdfIntent)
        }

        binding.buttonCandidacyNew.setOnClickListener { createJobApplication() }
    }

    private fun createJobApplication() {
        
    }

    companion object {
        val TAG = JobApplicationCreateFragment::class.java.simpleName
    }
}