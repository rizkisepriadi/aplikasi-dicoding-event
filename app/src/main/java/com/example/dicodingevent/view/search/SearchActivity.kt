package com.example.dicodingevent.view.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.remote.response.EventResponse
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.data.remote.retrofit.ApiService
import com.example.dicodingevent.databinding.ActivitySearchBinding
import com.example.dicodingevent.viewmodel.EventAdapter
import com.example.dicodingevent.viewmodel.MainViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var eventAdapter: EventAdapter
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this) as ViewModelProvider.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventAdapter = EventAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = eventAdapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                setupSearchView()
                searchBar.setText(keyword)
                searchView.hide()
                false
            }
        }
    }

    private fun setupSearchView() {
        binding.apply {
            searchView.setupWithSearchBar(binding?.searchBar)

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                mainViewModel.searchEvent(keyword)

                val currentText = searchView.text

                searchView.hide()

                searchView.editText.text = currentText

                true
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
