package com.dev.hirelink.modules.core.employer.candidacy.detail

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.components.dateAsString
import com.dev.hirelink.databinding.ActivityCandidacyDetailBinding
import com.dev.hirelink.dialogs.PDFViewerDialog
import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.employer.EmployerProfilActivity
import com.dev.hirelink.modules.core.jobapplication.JobApplicationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CandidacyDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCandidacyDetailBinding
    private lateinit var jobApplication: JobApplication
    private val compositeDisposable = CompositeDisposable()
    private val jobApplicationViewModel: JobApplicationViewModel by viewModels {
        JobApplicationViewModel.JobApplicationViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).jobApplicationRepository
        )
    }
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCandidacyDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        supportActionBar?.hide()

        customLoadingOverlay = CustomLoadingOverlay(
            this,
            R.id.candidacy_detail_root_constraint_layout,
            R.layout.loading_overlay_centered
        )
        //Log.d(javaClass.simpleName, intent.getIntExtra("jobApplicationID", 0).toString())

        bindListeners()
    }

    override fun onResume() {
        super.onResume()
        fetchJobApplications()
    }

    private fun bindListeners() {
        binding.imgBtnArrowLeft.setOnClickListener {
            finish()
        }

        binding.imgBtnProfileCandidacyList.setOnClickListener {
            startActivity(Intent(this, EmployerProfilActivity::class.java))
        }

        binding.cvConstraintLayout.setOnClickListener {
            PDFViewerDialog(jobApplication.documents?.get(0)!!)
                .show(supportFragmentManager, "pdfViewer")
        }

        binding.coverLetterConstraintLayout.setOnClickListener {
            PDFViewerDialog(jobApplication.documents?.get(1)!!)
                .show(supportFragmentManager, "pdfViewer")
        }

        binding.acceptMaterialCardView.setOnClickListener {
            updateJobApplicationState(
                JobApplicationState.ACCEPTED
            )
        }
        binding.refusedMaterialCardView.setOnClickListener {
            updateJobApplicationState(
                JobApplicationState.REFUSED
            )
        }

        binding.callConstraintLayout.setOnClickListener { doCall() }
        binding.sendMsgConstraintLayout.setOnClickListener { doMessage() }
        binding.sendEmailConstraintLayout.setOnClickListener { doSendMail() }

    }

    private fun doCall() {
        val phoneNumber = jobApplication.applicant?.phoneNumber
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun doMessage() {
        val uri = Uri.parse("smsto:${jobApplication.applicant?.phoneNumber}")
        val it = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(it)
    }

    private fun doSendMail() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(jobApplication.applicant?.email!!))

        email.type = "message/rfc822"

        startActivity(Intent.createChooser(email, "Choose an Email client :"))
    }

    private fun updateJobApplicationState(state: JobApplicationState) {
        if (state.name == jobApplication.state)
            return

        customLoadingOverlay.showLoading()

        jobApplication.state = state.name

        val disposable = jobApplicationViewModel
            .update(jobApplication)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe({ fetched ->
                this.jobApplication = fetched
                initLayout()
            },
                { error: Throwable -> error.printStackTrace() }
            )

        compositeDisposable.add(disposable)
    }

    private fun fetchJobApplications() {
        customLoadingOverlay.showLoading()
        val jobApplicationID = intent.getIntExtra("jobApplicationID", 0)
        if (jobApplicationID == 0) {
            throw Exception("You me must provide a jobApplication ID")
        }

        val disposable = jobApplicationViewModel
            .findById(jobApplicationID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe({ fetched ->
                this.jobApplication = fetched
                initLayout()
            },
                { error: Throwable -> error.printStackTrace() }
            )

        compositeDisposable.add(disposable)
    }

    private fun initLayout() {
        binding.candidateDetailInitials.text = listOfNotNull(
            jobApplication.applicant?.firstName?.get(0),
            jobApplication.applicant?.lastName?.get(0)
        ).joinToString("")

        binding.candidateFullname.text = listOfNotNull(
            jobApplication.applicant?.firstName,
            jobApplication.applicant?.lastName
        ).joinToString(" ")

        binding.applicationDate.text = dateAsString(jobApplication.createdAt!!)

        val stateIndicator: View = binding.stateIndicator

        val stateIndicatorBg = stateIndicator.background as GradientDrawable
        stateIndicatorBg.setColor(getColor(JobApplicationState.valueOf(jobApplication.state!!).colorResourceId))

        binding.candidacyStateText.text =
            getString(JobApplicationState.valueOf(jobApplication.state!!).stringResourceId)

        binding.candidacyStateText.setTextColor(getColor(JobApplicationState.valueOf(jobApplication.state!!).colorResourceId))

    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}