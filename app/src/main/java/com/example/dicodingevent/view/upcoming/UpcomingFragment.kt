package com.example.dicodingevent.view.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.viewmodel.MainViewModel
import com.example.dicodingevent.viewmodel.UpcomingAdapter
import com.example.dicodingevent.viewmodel.ViewModelFactory

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity()) as ViewModelProvider.Factory
    }

    private lateinit var adapter: UpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupAdapter()
        observeViewModel()

        val root: View = binding.root

        return root
    }

    private fun setupRecyclerView() {
        binding.apply {
            val upcomingEvent = LinearLayoutManager(requireContext())
            rvUpcoming.layoutManager = upcomingEvent
            val itemUpcomingEvent =
                DividerItemDecoration(requireContext(), upcomingEvent.orientation)
            rvUpcoming.addItemDecoration(itemUpcomingEvent)
        }
    }

    private fun setupAdapter() {
        adapter = UpcomingAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        binding.rvUpcoming.adapter = adapter
    }

    private fun observeViewModel() {
        binding.apply {
            mainViewModel.upcomingEvent.observe(viewLifecycleOwner) { listItems ->
                setUpcomingEvent(listItems)
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
    }

    private fun setUpcomingEvent(lifeUpcomingEvent: List<ListEventsItem?>) {
        adapter.submitList(lifeUpcomingEvent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}