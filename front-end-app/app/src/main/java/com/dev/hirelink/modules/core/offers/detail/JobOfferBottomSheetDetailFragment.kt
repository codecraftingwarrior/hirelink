package com.dev.hirelink.modules.core.offers.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.components.dateAsString
import com.dev.hirelink.components.toTimeAgo
import com.dev.hirelink.databinding.FragmentBottomSheetJobOfferDetailBinding
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.jobapplication.create.JobApplicationCreateFragment
import com.dev.hirelink.modules.core.offers.list.JobOfferViewModel
import com.dev.hirelink.network.auth.AuthRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

var mapView: MapView? = null

class JobOfferBottomSheetDetailFragment(
    private val jobOffer: JobOffer
) : BottomSheetDialogFragment() {

    private lateinit var routeLineOptions: MapboxRouteLineOptions
    private lateinit var binding: FragmentBottomSheetJobOfferDetailBinding
    private lateinit var currentUser: ApplicationUser
    private lateinit var authRepository: AuthRepository
    private lateinit var jobOfferViewModel: JobOfferViewModel
    private lateinit var sharedPrefs: SharedPreferenceManager
    private lateinit var fetchedJobOffer: JobOffer
    private lateinit var dialog: DialogInterface
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_sheet_job_offer_detail,
            container,
            false
        )
        sharedPrefs = SharedPreferenceManager(requireContext())
        jobOfferViewModel = (requireActivity() as BaseActivity).jobOfferViewModel
        authRepository = (requireActivity() as BaseActivity).authRepository

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        this.dialog = dialog
        dialog.setOnShowListener { configureFullScreenMode(it) }
        return dialog
    }

    private fun configureFullScreenMode(it: DialogInterface?) {
        val bottomSheetDialog = it as BottomSheetDialog
        val parentLayout =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        parentLayout?.let { it ->
            val behaviour = BottomSheetBehavior.from(it)
            setupFullHeight(it)
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchJobOfferById()
        binding.buttonJobOfferDetailApply.setOnClickListener { applyForJob() }
    }

    private fun applyForJob() {
        (JobApplicationCreateFragment(jobOffer)).show(
            (context as BaseActivity).supportFragmentManager,
            JobApplicationCreateFragment.TAG
        )
    }

    private fun fetchCurrentUser() {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                currentUser = applicationUser ?: ApplicationUser()
                binding.buttonJobOfferDetailApply.visibility =
                    if (currentUser.id != null && currentUser.role?.code == RoleType.APPLICANT.code) View.VISIBLE else View.GONE
            }

        compositeDisposable.add(disposable)
    }


    private fun fetchJobOfferById() {
        showLoader()
        val disposable = jobOfferViewModel
            .jobOfferRepository
            .findById(jobOffer.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideLoader() }
            .subscribe(
                { fetchedJobOffer ->
                    this.fetchedJobOffer = fetchedJobOffer
                    Log.d(
                        TAG,
                        "${fetchedJobOffer.lat?.toDouble()} ${fetchedJobOffer.lng?.toDouble()}"
                    )
                    fetchCurrentUser()
                    initLayout()
                },
                { error: Throwable ->
                    error.printStackTrace()
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            )
        compositeDisposable.add(disposable)

    }

    private fun initLayout() {
        with(binding) {
            val timeago = toTimeAgo(jobOffer.createdAt ?: "2023-05-02T13:50:37+00:00")
            textViewCompanyNameJobOfferDetail.text = fetchedJobOffer.owner?.company?.name
            textViewJobTitleJobOfferDetail.text = fetchedJobOffer.title
            textViewCompanyInitialsJobOfferDetail.text =
                fetchedJobOffer.owner?.company?.name?.get(0).toString()
            textViewCompanyLocationJobOfferDetail.text = fetchedJobOffer.address
            textViewPublishedAtJobOfferDetail.text = if (fetchedJobOffer.applicantCount!! > 0) {
                getString(R.string.published_at_applicants, timeago, fetchedJobOffer.applicantCount)
            } else {
                getString(R.string.published_at_applicants_first, timeago)
            }

            textViewContractTypeJobOfferDetail.text = getString(
                R.string.offer_category_contract_profession,
                fetchedJobOffer.contractType?.name,
                fetchedJobOffer.category?.name,
                fetchedJobOffer.profession?.name
            )
            textViewSalaryRangeJobOfferDetail.text = getString(
                R.string.salary_range_str,
                fetchedJobOffer.minSalary,
                fetchedJobOffer.maxSalary
            )

            textViewDateRangeJobOfferDetail.text = getString(
                R.string.date_range,
                dateAsString(fetchedJobOffer.fromDate ?: "2023-05-02T13:50:37+00:00"),
                dateAsString(fetchedJobOffer.toDate ?: "2023-05-02T13:50:37+00:00")
            )

            textViewOfferDetailDescription.text = fetchedJobOffer.description
        }

        initMap()
    }

    private fun initMap() {
        mapView = binding.mapViewJobOfferDetail

        val cameraPosition = CameraOptions.Builder()
            .zoom(9.0)
            .center(
                Point.fromLngLat(
                    fetchedJobOffer.lng?.toDouble()!!,
                    fetchedJobOffer.lat?.toDouble()!!
                )
            )
            .build()

        mapView?.getMapboxMap()?.apply {
            setCamera(cameraPosition)
            loadStyleUri(Style.MAPBOX_STREETS) { addAnnotationToMap() }
        }

    }

    private fun addAnnotationToMap() {
        val jobOfferPoint = Point.fromLngLat(
            fetchedJobOffer.lng?.toDouble()!!,
            fetchedJobOffer.lat?.toDouble()!!
        )

        bitmapFromDrawableRes(requireContext(), R.drawable.red_marker)?.let {
            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(jobOfferPoint)
                .withIconImage(it)
            pointAnnotationManager?.create(pointAnnotationOptions)
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    private fun showLoader() {
        binding.progressBarJobOfferFetching.visibility = View.VISIBLE
        val rootConstraintLayout = binding.jobOfferDetailRootConstraintLayout

        for (i in 0 until rootConstraintLayout.childCount) {
            val child: View = rootConstraintLayout.getChildAt(i)
            if (child.id != R.id.progress_bar_job_offer_fetching)
                child.visibility = View.GONE
        }
    }

    private fun hideLoader() {
        binding.progressBarJobOfferFetching.visibility = View.GONE
        val rootConstraintLayout = binding.jobOfferDetailRootConstraintLayout

        for (i in 0 until rootConstraintLayout.childCount) {
            val child: View = rootConstraintLayout.getChildAt(i)
            if (child.id != R.id.progress_bar_job_offer_fetching)
                child.visibility = View.VISIBLE
        }
    }

    companion object {
        val TAG = JobOfferBottomSheetDetailFragment::class.java.simpleName
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}