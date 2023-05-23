package com.dev.hirelink.modules.core.offers.list.filter

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.models.Company
import com.dev.hirelink.models.Profession
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.offers.list.filter.company.CompanyFilterChooseFragment
import com.dev.hirelink.modules.core.offers.list.filter.profession.ProfessionFilterChooseFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


class JobOfferFilterBottomSheetFragment() : BottomSheetDialogFragment() {
    private var chosenProfessions: MutableList<Profession?>? = mutableListOf()
    private var chosenCompanies: MutableList<Company?>? = mutableListOf()
    private lateinit var binding: com.dev.hirelink.databinding.FragmentBottomSheetFilterBinding
    private lateinit var criteria: JobOfferFilterViewModel.JobOfferFilterCriteria
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
        criteria = filterViewModel.getCriteria() ?: JobOfferFilterViewModel.JobOfferFilterCriteria()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = filterViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindListener()

        attachObservers()
    }

    private fun attachObservers() {
        filterViewModel.criteria.observe(viewLifecycleOwner) {
            binding.editTextStartDate.setText(it.fromDate)
            binding.editTextEndDate.setText(it.toDate)
            binding.editTextMinSalary.setText(if (it.minSalary == null) "" else it.minSalary.toString())
            binding.editTextMaxSalary.setText(if (it.maxSalary == null) "" else it.maxSalary.toString())
            this.criteria = it
        }

        filterViewModel.chosenCompanies.observe(viewLifecycleOwner) {
            binding.textViewEmployerFilterVal.text = it?.map { c -> c?.name }?.joinToString(", ")
            chosenCompanies = it
            if (binding.textViewEmployerFilterVal.text.isNullOrEmpty())
                binding.textViewEmployerFilterVal.text = getString(R.string.company_filter_hint)
        }

        filterViewModel.chosenProfessions.observe(viewLifecycleOwner) {
            binding.textViewProfessionFilterVal.text = it?.map { c -> c?.name }?.joinToString(", ")
            chosenProfessions = it
            if (binding.textViewProfessionFilterVal.text.isNullOrEmpty())
                binding.textViewProfessionFilterVal.text =
                    getString(R.string.profession_filter_hint)
        }
    }

    private fun bindListener() {

        binding.editTextStartDate.setOnClickListener {
            showDatePicker { selectedDate ->
                val startDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDate))

                binding.editTextStartDate.setText(startDate)
                criteria.fromDate = startDate
            }
        }

        binding.editTextEndDate.setOnClickListener {
            showDatePicker { selectedDate ->
                val endDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date(selectedDate))

                binding.editTextEndDate.setText(endDate)
                criteria.toDate = endDate
            }
        }

        binding.buttonApply.setOnClickListener {

            try {
                criteria.minSalary = binding.editTextMinSalary.text?.toString()?.toFloat()
            } catch (e: Exception) {
                criteria.minSalary = null
            }

            try {
                criteria.maxSalary = binding.editTextMaxSalary.text?.toString()?.toFloat()
            } catch (e: Exception) {
                criteria.maxSalary = null
            }

            criteria.chosenCompanyIds = chosenCompanies?.map { it?.id!! }
            criteria.chosenProfessionIds = chosenProfessions?.map { it?.id!! }

            filterViewModel.updateCriteria(criteria)
            dismiss()
        }

        binding.buttonModifyEmployer.setOnClickListener {
            (CompanyFilterChooseFragment()).show(
                childFragmentManager,
                CompanyFilterChooseFragment.TAG
            )
        }

        binding.buttonModifyProfession.setOnClickListener {
            (ProfessionFilterChooseFragment()).show(
                childFragmentManager,
                ProfessionFilterChooseFragment.TAG
            )
        }


        binding.rootConstraintLayoutJobOfferFilter.setOnClickListener {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.hideSoftInputFromWindow(
                binding.rootConstraintLayoutJobOfferFilter.windowToken,
                0
            )
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

        try {
            this.criteria.minSalary = binding.editTextMinSalary.text.toString().toFloat()
        } catch (e: Exception) {
            this.criteria.minSalary = null
        }

        try {
            this.criteria.maxSalary = binding.editTextMaxSalary.text.toString().toFloat()
        } catch (e: Exception) {
            this.criteria.maxSalary = null
        }

        filterViewModel.updateCriteria(this.criteria)
    }


    companion object {
        val TAG = JobOfferFilterBottomSheetFragment::class.java.simpleName
    }

}