package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.R
import com.dev.hirelink.data.MockDataSource
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep4Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.adapters.PlanItemAdapter
import com.dev.hirelink.modules.auth.register.fragments.StepFragment

class EmployerRegisterStep4Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep4Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step4,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.buttonNext.setOnClickListener { updatePlanForUser() }
    }

    private fun updatePlanForUser() {
        listener.onNextButtonTouched(RegistrationStep.STEP_4)
    }

    private fun initRecyclerView() {
        val plans = MockDataSource().loadPlans()

        val recyclerView = binding.recyclerViewPlan
        recyclerView.adapter = PlanItemAdapter(requireContext(), plans)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
    }
}