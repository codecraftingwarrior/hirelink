package com.dev.hirelink.modules.core.offers.list.filter.company

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.models.Company
import com.dev.hirelink.models.PaginatedResourceWrapper
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.w3c.dom.Text


class CompanyFilterChooseFragment : BottomSheetDialogFragment() {
    private lateinit var binding: com.dev.hirelink.databinding.FragmentCompanyFilterChooseBinding
    private lateinit var companies: MutableList<Company?>
    private var matchedCompanies = mutableListOf<Company?>()
    private lateinit var jobOfferFilterViewModel: JobOfferFilterViewModel
    private lateinit var dialog: DialogInterface
    private var companySearchQuery: String? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        this.dialog = dialog
        // dialog.setOnShowListener { configureFullScreenMode(it) }
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_company_filter_choose,
            container,
            false
        );
        binding.lifecycleOwner = viewLifecycleOwner
        jobOfferFilterViewModel = (requireActivity() as BaseActivity).jobOfferListfilterViewModel
        fetchCompanies { data ->
            companies = data.items ?: mutableListOf()
            buildChips()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindListeners(view)
        setupSearchField()

    }

    private fun setupSearchField() {
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            matchedCompanies.map { it?.name })
        (binding.editTextCompanySearch as MaterialAutoCompleteTextView).setAdapter(adapter)


        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                companySearchQuery = (binding.editTextCompanySearch.text.toString().trim()) ?: ""
                if (companySearchQuery!!.isNotEmpty() && companySearchQuery!!.length >= 3) {
                    fetchCompanies(showLoader = false) {
                        matchedCompanies = it.items ?: mutableListOf()
                        adapter.clear()
                        val concernedCompanies = matchedCompanies.filter { c ->
                            !companies.map { ci -> ci?.id }.contains(c?.id)
                        }
                        companies.addAll(concernedCompanies)
                        adapter.addAll(concernedCompanies.map { c -> c?.name })
                        adapter.notifyDataSetChanged()
                    }
                }

            }

        }

        binding.editTextCompanySearch.addTextChangedListener(watcher)

    }

    private fun bindListeners(view: View) {
        binding.buttonDoneCompanyFilter.setOnClickListener {
            val checkedCompanyIds = binding
                .chipGroupCompanyChoose
                .checkedChipIds
                .map { (view.findViewById(it) as Chip).tag.toString().split("company_")[1] }

            Log.d(javaClass.simpleName, checkedCompanyIds.toString())

            val selectedCompanies =
                companies.filter { checkedCompanyIds.contains(it?.id.toString()) }.toMutableList()

            jobOfferFilterViewModel.updateChosenCompanies(selectedCompanies)
            dismiss()
        }

        binding.rootConstraintLayoutBottomSheetCompanySearch.setOnClickListener {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.hideSoftInputFromWindow(
                binding.rootConstraintLayoutBottomSheetCompanySearch.windowToken,
                0
            )
        }

        binding.editTextCompanySearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                configureFullScreenMode(dialog)
        }

        binding.editTextCompanySearch.setOnItemClickListener { parent, _, position, _ ->
            binding.editTextCompanySearch.setText("")
            binding.editTextCompanySearch.clearFocus()
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.editTextCompanySearch.windowToken, 0)

            val selectedText = parent.getItemAtPosition(position).toString()
            val chipGroup = binding.chipGroupCompanyChoose

            val chip = createChip(chipGroup, matchedCompanies.find { it?.name == selectedText })

            chipGroup.addView(chip)

        }

    }

    private fun showLoader() {
        binding.progressBarCompanyFetch.visibility = View.VISIBLE
        binding.chipGroupCompanyChoose.visibility = View.GONE
    }

    private fun hideLoader() {
        binding.progressBarCompanyFetch.visibility = View.GONE
        binding.chipGroupCompanyChoose.visibility = View.VISIBLE
    }

    private fun fetchCompanies(
        showLoader: Boolean = true,
        onDataReceived: (data: PaginatedResourceWrapper<Company>) -> Unit
    ) {
        if (showLoader)
            showLoader()
        val disposable = jobOfferFilterViewModel
            .companyRepository
            .findAll(name = companySearchQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideLoader() }
            .subscribe(onDataReceived) { error: Throwable -> error.printStackTrace() }

        compositeDisposable.add(disposable)

    }

    private fun buildChips() {
        val chipGroup = binding.chipGroupCompanyChoose
        for (company in companies) {
            val chip = createChip(chipGroup, company)

            chipGroup.addView(chip)
        }
    }

    private fun createChip(
        chipGroup: ChipGroup,
        company: Company?
    ): Chip {
        val chip = layoutInflater.inflate(R.layout.choice_chip_item, chipGroup, false) as Chip
        chip.id = View.generateViewId()
        chip.tag = "company_${company?.id}"
        chip.text = company?.name ?: ""
        chip.isClickable = true
        chip.isFocusable = true
        chip.isCheckedIconVisible = true
        chip.isCheckedIconVisible = true
        chip.isChecked = jobOfferFilterViewModel.chosenCompanies.value
            ?.map { it?.id }
            ?.contains(company?.id) ?: false
        return chip
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {
        val TAG = CompanyFilterChooseFragment::class.java.simpleName
    }
}