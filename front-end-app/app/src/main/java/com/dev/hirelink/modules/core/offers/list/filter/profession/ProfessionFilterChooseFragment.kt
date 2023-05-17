package com.dev.hirelink.modules.core.offers.list.filter.profession

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentProfessionFilterChooseBinding
import com.dev.hirelink.models.Company
import com.dev.hirelink.models.PaginatedResourceWrapper
import com.dev.hirelink.models.Profession
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

class ProfessionFilterChooseFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentProfessionFilterChooseBinding
    private lateinit var professions: MutableList<Profession?>
    private var matchedProfessions = mutableListOf<Profession?>()
    private lateinit var jobOfferFilterViewModel: JobOfferFilterViewModel
    private lateinit var dialog: DialogInterface
    private var professionSearchQuery: String? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        this.dialog = dialog
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
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profession_filter_choose,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        jobOfferFilterViewModel = (requireActivity() as BaseActivity).jobOfferListfilterViewModel
        fetchProfessions { data ->
            professions = data.items ?: mutableListOf()
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
            matchedProfessions.map { it?.name })
        (binding.editTextProfessionSearch as MaterialAutoCompleteTextView).setAdapter(adapter)


        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                professionSearchQuery =
                    (binding.editTextProfessionSearch.text.toString().trim()) ?: ""
                if (professionSearchQuery!!.isNotEmpty() && professionSearchQuery!!.length >= 3) {
                    fetchProfessions(showLoader = false) {
                        matchedProfessions = it.items ?: mutableListOf()
                        adapter.clear()
                        val concernedProfessions = matchedProfessions.filter { p ->
                            !professions.map { pr -> pr?.id }.contains(p?.id)
                        }
                        professions.addAll(concernedProfessions)
                        adapter.addAll(concernedProfessions.map { p -> p?.name })
                        adapter.notifyDataSetChanged()
                    }
                }

            }

        }

        binding.editTextProfessionSearch.addTextChangedListener(watcher)

    }

    private fun bindListeners(view: View) {
        binding.buttonDoneProfessionFilter.setOnClickListener {
            val checkedProfessions = binding
                .chipGroupProfessionChoose
                .checkedChipIds
                .map { (view.findViewById(it) as Chip).tag.toString().split("profession_")[1] }


            val selectedProfessions =
                professions.filter { checkedProfessions.contains(it?.id.toString()) }
                    .toMutableList()

            Log.d(TAG, selectedProfessions.toString())

            jobOfferFilterViewModel.updateChosenProfessions(selectedProfessions)

            dismiss()
        }

        binding.rootConstraintLayoutBottomSheetProfessionSearch.setOnClickListener {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.hideSoftInputFromWindow(
                binding.rootConstraintLayoutBottomSheetProfessionSearch.windowToken,
                0
            )
        }

        binding.editTextProfessionSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                configureFullScreenMode(dialog)
        }

        binding.editTextProfessionSearch.setOnItemClickListener { parent, _, position, _ ->
            binding.editTextProfessionSearch.setText("")
            binding.editTextProfessionSearch.clearFocus()
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.editTextProfessionSearch.windowToken, 0)

            val selectedText = parent.getItemAtPosition(position).toString()
            val chipGroup = binding.chipGroupProfessionChoose

            val chip = createChip(chipGroup, matchedProfessions.find { it?.name == selectedText })

            chipGroup.addView(chip)

        }

    }

    private fun showLoader() {
        binding.progressBarCompanyFetch.visibility = View.VISIBLE
        binding.chipGroupProfessionChoose.visibility = View.GONE
    }

    private fun hideLoader() {
        binding.progressBarCompanyFetch.visibility = View.GONE
        binding.chipGroupProfessionChoose.visibility = View.VISIBLE
    }

    private fun fetchProfessions(
        showLoader: Boolean = true,
        onDataReceived: (data: PaginatedResourceWrapper<Profession>) -> Unit
    ) {
        if (showLoader)
            showLoader()
        val disposable = jobOfferFilterViewModel
            .professionRepository
            .findAll(name = professionSearchQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideLoader() }
            .subscribe(onDataReceived) { error: Throwable -> error.printStackTrace() }

        compositeDisposable.add(disposable)

    }

    private fun buildChips() {
        val chipGroup = binding.chipGroupProfessionChoose
        for (profession in professions) {
            val chip = createChip(chipGroup, profession)

            chipGroup.addView(chip)
        }
    }

    private fun createChip(
        chipGroup: ChipGroup,
        profession: Profession?
    ): Chip {
        val chip = layoutInflater.inflate(R.layout.choice_chip_item, chipGroup, false) as Chip
        chip.id = View.generateViewId()
        chip.tag = "profession_${profession?.id}"
        chip.text = profession?.name ?: ""
        chip.isClickable = true
        chip.isFocusable = true
        chip.isCheckedIconVisible = true
        chip.isCheckedIconVisible = true
        chip.isChecked = jobOfferFilterViewModel.chosenProfessions.value
            ?.map { it?.id }
            ?.contains(profession?.id) ?: false
        return chip
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


    companion object {
        val TAG: String = ProfessionFilterChooseFragment::class.java.simpleName
    }


}