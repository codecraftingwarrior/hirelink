package com.dev.hirelink.modules.core.offers.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.hirelink.R
import com.dev.hirelink.data.MockDataSource
import com.dev.hirelink.databinding.FragmentJobOfferListBinding
import com.dev.hirelink.modules.auth.register.adapters.PlanItemAdapter
import com.dev.hirelink.modules.core.offers.adapter.JobOfferItemAdapter

class JobOfferListFragment : Fragment() {
    private lateinit var binding: FragmentJobOfferListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_job_offer_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val jobOffers = MockDataSource().loadJobOffers()

        val recyclerView = binding.recyclerViewOfferList
        recyclerView.adapter = JobOfferItemAdapter(requireContext(), jobOffers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }
}