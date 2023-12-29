package com.example.event_viewer.ui.home

import EventAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.event_viewer.R
import com.example.event_viewer.database.AppDatabase
import com.example.event_viewer.database.DatabaseBuilder
import com.example.event_viewer.database.EventDao
import com.example.event_viewer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var eventDao: EventDao

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Assuming you have a way to obtain EventDao

        // Initialize EventDao
        initEventDao()

        // Initialize ViewModel and pass EventDao
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.init(eventDao)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerViewEvents
        val adapter = EventAdapter(listOf()) // Initialize with an empty list
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        homeViewModel.events.observe(viewLifecycleOwner, Observer { events ->
            adapter.setEvents(events)
        })

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }


        return root
    }

    private fun refreshData() {
        // Trigger data reloading
        homeViewModel.loadEvents() // Assuming you have a method like this in your ViewModel

        // Once data is loaded, make sure to stop the refresh animation
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun initEventDao() {
        val database = DatabaseBuilder.getDatabase(requireContext())
        eventDao = database.eventDao()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}