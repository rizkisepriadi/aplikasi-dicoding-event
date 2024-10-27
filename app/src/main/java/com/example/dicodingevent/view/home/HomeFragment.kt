package com.example.dicodingevent.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.view.search.SearchActivity
import com.example.dicodingevent.viewmodel.FinishedAdapter
import com.example.dicodingevent.viewmodel.MainViewModel
import com.example.dicodingevent.viewmodel.UpcomingAdapter
import com.example.dicodingevent.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity()) as ViewModelProvider.Factory
    }

    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var finishedAdapter: FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupRecyclerViews()
        setupSearchButton()
        observeViewModel()
    }

    private fun setupAdapters() {
        upcomingAdapter = UpcomingAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        finishedAdapter = FinishedAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }
    }

    private fun setupRecyclerViews() {
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }
    }

    private fun setupSearchButton() {
        binding.searchButton.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        mainViewModel.upcomingEvent.observe(viewLifecycleOwner) { listItems ->
            setUpcomingList(listItems)
            mainViewModel.zeroErrorMessage()
        }

        mainViewModel.finishedEvent.observe(viewLifecycleOwner) { listItems ->
            setFinishedList(listItems)
            mainViewModel.zeroErrorMessage()
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        mainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                mainViewModel.zeroErrorMessage()
            }
        }
    }

    private fun setUpcomingList(listEvents: List<ListEventsItem?>) {
        val list = if (listEvents.size > 5) listEvents.take(5) else listEvents
        upcomingAdapter.submitList(list)
    }

    private fun setFinishedList(listEvents: List<ListEventsItem?>) {
        val list = if (listEvents.size > 5) listEvents.take(5) else listEvents
        finishedAdapter.submitList(list)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
