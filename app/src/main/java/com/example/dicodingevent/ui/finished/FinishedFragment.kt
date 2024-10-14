package com.example.dicodingevent.ui.finished

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
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.DetailEventActivity

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel by viewModels<FinishedViewModel>()

    private lateinit var adapter: FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = FinishedAdapter(FinishedAdapter.DIFF_CALLBACK)
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.adapter = adapter

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishedViewModel.finished.observe(viewLifecycleOwner) { listEvents ->
            setFinishedList(listEvents)
        }

        finishedViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError == true) {
                finishedViewModel.message.value?.let { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                } ?: Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }

        finishedViewModel.detailFinished.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), DetailEventActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        return root
    }

    private fun setFinishedList(listEvents: List<ListEventsItem>?) {
        adapter.submitList(listEvents)

        adapter.onItemClickListener = { event ->
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
