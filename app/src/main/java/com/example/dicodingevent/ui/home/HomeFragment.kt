package com.example.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.DetailEventActivity
import com.example.dicodingevent.ui.SearchActivity
import com.example.dicodingevent.ui.finished.FinishedAdapter
import com.example.dicodingevent.ui.finished.FinishedViewModel
import com.example.dicodingevent.ui.upcoming.UpcomingAdapter
import com.example.dicodingevent.ui.upcoming.UpcomingViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val finishedViewModel by viewModels<FinishedViewModel>()
    private val upcomingViewModel by viewModels<UpcomingViewModel>()

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

        upcomingAdapter = UpcomingAdapter(UpcomingAdapter.DIFF_CALLBACK)
        binding.rvUpcomingEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.adapter = upcomingAdapter

        finishedAdapter = FinishedAdapter(FinishedAdapter.DIFF_CALLBACK)
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(context)
        binding.rvFinishedEvents.adapter = finishedAdapter

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        upcomingViewModel.upcoming.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let {
                setUpcomingList(it.take(5))
            }
        }

        finishedViewModel.finished.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let {
                setFinishedList(it.take(5))
            }
        }

        upcomingViewModel.detailUpcoming.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), DetailEventActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        finishedViewModel.detailFinished.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), DetailEventActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        binding.searchButton.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpcomingList(listEvents: List<ListEventsItem>) {
        upcomingAdapter.submitList(listEvents)

        upcomingAdapter.onItemClickListener = { event ->
            event.id?.let { upcomingViewModel.detailUpcomingEvent(it) }
        }
    }

    private fun setFinishedList(listEvents: List<ListEventsItem>) {
        finishedAdapter.submitList(listEvents)

        finishedAdapter.onItemClickListener = { event ->
            event.id?.let { finishedViewModel.detailFinishedEvents(it) }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
