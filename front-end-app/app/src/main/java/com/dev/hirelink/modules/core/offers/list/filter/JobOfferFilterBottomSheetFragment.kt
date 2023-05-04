package com.dev.hirelink.modules.core.offers.list.filter

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentBottomSheetFilterBinding
import com.dev.hirelink.modules.core.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


class JobOfferFilterBottomSheetFragment() : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetFilterBinding
    private var criteria = JobOfferFilterViewModel.JobOfferFilterCriteria()
    private lateinit var filterViewModel: JobOfferFilterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_bottom_sheet_filter,
            container,
            false
        )

        filterViewModel = (requireActivity() as BaseActivity).jobOfferListfilterViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = filterViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindListener()

        filterViewModel.criteria.observe(viewLifecycleOwner) {
            binding.editTextStartDate.setText(it.fromDate)
            binding.editTextEndDate.setText(it.toDate)
            binding.editTextMinSalary.setText(it.minSalary.toString())
            binding.editTextMaxSalary.setText(it.maxSalary.toString())
            this.criteria = it
        }
    }

    private fun bindListener() {

        binding.editTextStartDate.setOnClickListener {
            showDatePicker { selectedDate ->
                val startDate =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDate))

                binding.editTextStartDate.setText(startDate)
                criteria.fromDate = startDate
            }
        }

        binding.editTextEndDate.setOnClickListener {
            showDatePicker { selectedDate ->
                val endDate = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(Date(selectedDate))

                binding.editTextEndDate.setText(endDate)
                criteria.toDate = endDate
            }
        }
    }

    private fun showDatePicker(onDateChoosen: (date: Long) -> Unit) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getString(R.string.date_select_prompt))
            .build()

        datePicker.addOnPositiveButtonClickListener(onDateChoosen)

        datePicker.show(parentFragmentManager, "datePicker")
    }

    override fun onStop() {
        super.onStop()

        this.criteria.minSalary = binding.editTextMinSalary.text.toString().toFloat()
        this.criteria.maxSalary = binding.editTextMaxSalary.text.toString().toFloat()

        filterViewModel.updateCriteria(this.criteria)
        Log.d(TAG, "saving ${this.criteria}")
    }


    companion object {
        val TAG = JobOfferFilterBottomSheetFragment::class.java.simpleName
    }

}