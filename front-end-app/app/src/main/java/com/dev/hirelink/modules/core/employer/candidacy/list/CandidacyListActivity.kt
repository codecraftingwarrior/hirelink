package com.dev.hirelink.modules.core.employer.candidacy.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityCandidacyListBinding
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.employer.EmployerProfilActivity
import com.dev.hirelink.modules.core.offers.JobOfferViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CandidacyListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCandidacyListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CandidacyItemAdapter
    private val jobOfferViewModel: JobOfferViewModel by viewModels {
        JobOfferViewModel.JobOfferViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).jobOfferRepository,
            (application as HirelinkApplication).contractTypeRepository,
            (application as HirelinkApplication).jobCategoryRepository,
            (application as HirelinkApplication).professionRepository,
            (application as HirelinkApplication).authRepository
        )
    }
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var jobApplications: MutableList<JobApplication?>
    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidacyListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setup()
    }

    private fun setup() {

        supportActionBar?.hide()

        customLoadingOverlay = CustomLoadingOverlay(
            this,
            R.id.root_constraint_layout_candidacy_list,
            R.layout.loading_overlay_centered
        )

        fetchJobApplications()

        bindListeners()
    }

    private fun bindListeners() {
        binding.imgBtnArrowLeft.setOnClickListener {
            finish()
        }

        binding.imgBtnProfileCandidacyList.setOnClickListener {
            startActivity(Intent(this, EmployerProfilActivity::class.java))
        }

    }

    private fun fetchJobApplications() {
        customLoadingOverlay.showLoading()
        val jobOfferID = intent.getIntExtra("jobOfferID", 0)
        if (jobOfferID == 0) {
            throw Exception("You me must provide a jobOffer ID")
        }

        val disposable = jobOfferViewModel
            .findJobApplicationsByJobOfferId(JobOffer(id = jobOfferID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe({ fetched ->
                this.jobApplications = fetched.jobApplications?.toMutableList()!!
                initRecyclerView()
            },
                { error: Throwable -> error.printStackTrace() }
            )

        compositeDisposable.add(disposable)
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerViewEmployerJobOfferList
        adapter = CandidacyItemAdapter(this, jobApplications)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}