package com.dev.hirelink.modules.core.jobapplication.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.uriToFile
import com.dev.hirelink.databinding.FragmentBottomSheetCandidacyNewBinding
import com.dev.hirelink.dto.BasicErrorResponse
import com.dev.hirelink.dto.DocumentDTO
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.jobapplication.JobApplicationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.NonDisposableHandle.parent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class JobApplicationCreateFragment(
    private val jobOffer: JobOffer
) : BottomSheetDialogFragment() {
    private lateinit var jobApplicationViewModel: JobApplicationViewModel
    private lateinit var binding: FragmentBottomSheetCandidacyNewBinding
    private val compositeDisposable = CompositeDisposable()
    private lateinit var currentUser: ApplicationUser
    private lateinit var cvUri: Uri
    private lateinit var coverLetterUri: Uri
    private lateinit var cvFilePickerActivityResult: ActivityResultLauncher<Intent>
    private lateinit var coverLetterFilePickerActivityResultLauncher: ActivityResultLauncher<Intent>
    private var cvPDFFile: File? = null
    private var coverLetterPDFFile: File? = null

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

        jobApplicationViewModel = (requireActivity() as BaseActivity).jobApplicationViewModel

        currentUser = (requireActivity() as BaseActivity).currentUser

        cvFilePickerActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    handlePdfSelection(data, "cv")
                    binding.buttonViewCvCandidacyNew.visibility = View.VISIBLE
                    if (cvPDFFile != null && coverLetterPDFFile != null)
                        binding.buttonCandidacyNew.visibility = View.VISIBLE;
                }
            }

        coverLetterFilePickerActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    handlePdfSelection(data, "cl")
                    binding.buttonViewCoverLetterCandidacyNew.visibility = View.VISIBLE
                    if (cvPDFFile != null && coverLetterPDFFile != null)
                        binding.buttonCandidacyNew.visibility = View.VISIBLE;
                }
            }

        return binding.root
    }

    @SuppressLint("Range")
    private fun handlePdfSelection(data: Intent?, type: String) {
        val uri: Uri = data?.data!!
        val uriString: String = uri.toString()
        var pdfName: String? = null
        when (type) {
            "cv" -> {
                cvPDFFile = uriToFile(uri, requireContext(), requireContext().contentResolver)
                Log.d(javaClass.simpleName, uri.toString())
            }
            "cl" -> {
                coverLetterPDFFile =
                    uriToFile(uri, requireContext(), requireContext().contentResolver)
            }
        }


        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = requireContext()
                    .contentResolver
                    .query(uri, null, null, null, null)

                if (cursor != null && cursor.moveToFirst()) {
                    pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    when (type) {
                        "cv" -> binding.textViewCvChooserHintCandidacyNew.text = pdfName
                        "cl" -> binding.textViewCoverLetterHintCandidacyNew.text = pdfName
                    }
                }
            } finally {
                cursor?.close()
            }
        }
    }

    private fun showPdfFromUri(uri: Uri?) {
        binding.pdfView.visibility = View.VISIBLE
        binding
            .pdfView
            .fromUri(uri)
            .defaultPage(0)
            .spacing(10)
            .enableSwipe(true)
            .load()
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
            val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            pdfIntent.type = "application/pdf"
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)

            cvFilePickerActivityResult.launch(pdfIntent)
        }

        binding.linearLayoutCoverLetterChoose.setOnClickListener {
            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
            pdfIntent.type = "application/pdf"
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)

            coverLetterFilePickerActivityResultLauncher.launch(pdfIntent)
        }

        binding.buttonCandidacyNew.setOnClickListener { createJobApplication() }
        binding.buttonViewCoverLetterCandidacyNew.setOnClickListener { showPdfFromUri(coverLetterUri) }
        binding.buttonViewCvCandidacyNew.setOnClickListener { showPdfFromUri(cvUri) }
    }

    private fun createJobApplication() {
        showLoader()
        val files = listOf<File>(cvPDFFile!!, coverLetterPDFFile!!)

        val cvDocument = DocumentDTO(
            title = "Resume of ${currentUser.firstName} ${currentUser.lastName} - ${jobOffer.title}",
            owner = currentUser.toIRI(),
            file = cvPDFFile,
            content = null
        )

        val clDocument = DocumentDTO(
            title = "Cover letter of ${currentUser.firstName} ${currentUser.lastName} - ${jobOffer.title}",
            owner = currentUser.toIRI(),
            file = cvPDFFile,
            content = null
        )
        val fieldsMap = mutableMapOf<String, RequestBody>()

        fieldsMap["jobOffer"] = MultipartBody.Part.createFormData("jobOffer", jobOffer.toIRI()).body
        fieldsMap["applicant"] = MultipartBody.Part.createFormData("applicant", currentUser.toIRI()).body

        val documents = listOf(cvDocument, clDocument)

        documents.forEachIndexed { index, document ->
            fieldsMap["documents[$index][title]"] = MultipartBody.Part.createFormData(
                "documents[$index][title]",
                document.title ?: ""
            ).body

            fieldsMap["documents[$index][content]"] = MultipartBody.Part.createFormData(
                "documents[$index][content]",
                document.content ?: ""
            ).body

            fieldsMap["documents[$index][owner]"] = MultipartBody.Part.createFormData(
                "documents[$index][owner]",
                document.owner ?: ""
            ).body
        }

        val fileParts = files.mapIndexed { index, file ->
            val fileRequestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("documents[$index][file]", file.name, fileRequestBody)
        }

        val disposable = jobApplicationViewModel
            .jobApplicationRepository
            .create(fieldsMap, fileParts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideLoader() }
            .subscribe(
                { jobApplication ->
                    Log.d(javaClass.simpleName, jobApplication.toString())
                    jobApplicationViewModel.onJobApplicationDone()
                    dismiss()
                },
                { error: Throwable -> handleError(error) }
            )

        compositeDisposable.add(disposable)
    }

    private fun showLoader() {
        binding.progressBarJobApplicationProcess.visibility = View.VISIBLE
        val rootConstraintLayout = binding.candidacyNewRootConstraintLayout

        for (i in 0 until rootConstraintLayout.childCount) {
            val child: View = rootConstraintLayout.getChildAt(i)
            if (child.id != R.id.progress_bar_job_application_process)
                child.visibility = View.GONE
        }
    }

    private fun hideLoader() {
        binding.progressBarJobApplicationProcess.visibility = View.GONE
        val rootConstraintLayout = binding.candidacyNewRootConstraintLayout
        for (i in 0 until rootConstraintLayout.childCount) {
            val child: View = rootConstraintLayout.getChildAt(i)
            if (child.id != R.id.progress_bar_job_application_process && child.id != R.id.pdfView)
                child.visibility = View.VISIBLE
        }
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is HttpException -> {
                try {
                    val errorResponse =
                        HttpExceptionParser.parse(error, BasicErrorResponse::class.java)
                    if (errorResponse.violations?.isEmpty() == false)
                        for (violation in errorResponse.violations)
                            Snackbar.make(
                                binding.pdfView,
                                violation.message ?: "no message",
                                Snackbar.LENGTH_SHORT
                            ).show()

                    Log.d(javaClass.simpleName, error.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(
                        binding.pdfView,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
            else -> {
                error.printStackTrace()
                Snackbar.make(
                    binding.pdfView,
                    getString(R.string.error_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {
        val TAG = JobApplicationCreateFragment::class.java.simpleName
    }
}