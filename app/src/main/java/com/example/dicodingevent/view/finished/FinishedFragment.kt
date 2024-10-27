package com.example.dicodingevent.view.finished

import android.os.Bundle
import android.util.Log
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
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.viewmodel.MainViewModel
import com.example.dicodingevent.viewmodel.FinishedAdapter
import com.example.dicodingevent.viewmodel.ViewModelFactory

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity()) as ViewModelProvider.Factory
    }

    private lateinit var adapter: FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupAdapter()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvFinished.addItemDecoration(itemDecoration)
    }

    private fun setupAdapter() {
        adapter = FinishedAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                    Log.d("FinishedFragment", "Event ID dikirim: $eventId")
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }
        binding.rvFinished.adapter = adapter
    }

    private fun observeViewModel() {
        mainViewModel.finishedEvent.observe(viewLifecycleOwner) { listItems ->
            setFinishedEvent(listItems)
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

    private fun setFinishedEvent(lifeFinishedEvent: List<ListEventsItem?>) {
        adapter.submitList(lifeFinishedEvent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
