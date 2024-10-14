package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.DetailResponse
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.ui.finished.FinishedViewModel
import com.example.dicodingevent.ui.finished.FinishedViewModel.Companion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    private val _upcoming = MutableLiveData<List<ListEventsItem>?>()
    val upcoming: LiveData<List<ListEventsItem>?> = _upcoming

    private val _detailUpcoming = MutableLiveData<Event?>()
    val detailUpcoming: LiveData<Event?> = _detailUpcoming

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _error

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    companion object {
        private const val TAG = "UpcomingViewModel"
        private const val EVENT_ID = 1
    }

    init {
        listUpcomingEvents()
    }

    private fun listUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListEvents(EVENT_ID)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _isLoading.value = false
                        _upcoming.value = it.listEvents as List<ListEventsItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.value = true
                    _message.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(p0: Call<EventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _error.value = true
                if (t is java.net.UnknownHostException) {
                    _message.value = "Tidak ada koneksi internet"
                } else {
                    _message.value = "Error: ${t.message}"
                }
            }
        })
    }

    fun detailUpcomingEvent(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<DetailResponse<Event>> {
            override fun onResponse(
                call: Call<DetailResponse<Event>>,
                response: Response<DetailResponse<Event>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _detailUpcoming.value = response.body()?.event
                } else {
                    _isLoading.value = false
                    _error.value = true
                    _message.value = "Error: ${response.message()}"
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<DetailResponse<Event>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                _message.value = "Error: ${t.message}"
                Log.e(
                    TAG,
                    "onFailure: ${t.message}"
                )
            }
        })
    }
}
