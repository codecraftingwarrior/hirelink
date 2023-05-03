package com.dev.hirelink.modules.core.jobapplication.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.hirelink.R
import com.dev.hirelink.data.MockDataSource
import com.dev.hirelink.databinding.FragmentJobApplicationListBinding

class JobApplicationListFragment : Fragment() {
    private lateinit var binding: FragmentJobApplicationListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_application_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val jobApplications = MockDataSource().loadJobApplications()

        val recyclerView = binding.recyclerViewJobApplicationList
        recyclerView.adapter = JobApplicationItemAdapter(requireContext(), jobApplications)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }

}