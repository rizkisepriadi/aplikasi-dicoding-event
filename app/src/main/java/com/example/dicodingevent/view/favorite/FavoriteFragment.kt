package com.example.dicodingevent.view.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.model.FavoriteEvent
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.viewmodel.FavoriteAdapter
import com.example.dicodingevent.viewmodel.MainViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity()) as ViewModelProvider.Factory
    }

    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupAdapter()
        observeViewModel()

        return requireNotNull(binding?.root) { "Binding is null!" }
    }

    private fun observeViewModel() {
        binding?.apply {
            val verticalLayout = LinearLayoutManager(requireContext())
            rvFavoriteEvent.layoutManager = verticalLayout
            val itemFavoriteDecoration = DividerItemDecoration(requireContext(), verticalLayout.orientation)
            rvFavoriteEvent.addItemDecoration(itemFavoriteDecoration)
        }
    }

    private fun setupAdapter() {
        adapter = FavoriteAdapter { eventId ->
            val bundle = Bundle().apply {
                eventId?.let { putInt("eventId", it) }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        binding?.rvFavoriteEvent?.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding?.apply {
            mainViewModel.allFavoriteEvents.observe(viewLifecycleOwner) { listEvent ->
                Log.d("FavoriteFragment", "Observed Favorite Events: $listEvent")
                setFavoriteEvents(listEvent)
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

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setFavoriteEvents(listEvent: List<FavoriteEvent?>?) {
        adapter.submitList(listEvent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}