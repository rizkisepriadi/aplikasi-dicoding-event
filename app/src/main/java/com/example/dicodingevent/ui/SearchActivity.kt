package com.example.dicodingevent.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.data.retrofit.ApiService
import com.example.dicodingevent.databinding.ActivitySearchBinding
import com.example.dicodingevent.ui.adapter.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiService()

        eventAdapter = EventAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = eventAdapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                performSearch(apiService, keyword)
                searchBar.setText(keyword)
                searchView.hide()
                false
            }
        }
    }

    private fun performSearch(apiService: ApiService, keyword: String) {
        showLoading(true)
        apiService.searchEvents(keyword = keyword).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val events = response.body()?.listEvents
                    Log.d("SearchActivity", "Events: $events")
                    if (!events.isNullOrEmpty()) {
                        eventAdapter.setEvents(events)
                        Toast.makeText(this@SearchActivity, "Found ${events.size} events", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SearchActivity, "No events found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
