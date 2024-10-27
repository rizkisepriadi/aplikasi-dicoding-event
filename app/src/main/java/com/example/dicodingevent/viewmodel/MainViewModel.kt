package com.example.dicodingevent.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.SettingPreferences
import com.example.dicodingevent.data.local.model.FavoriteEvent
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: SettingPreferences,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem?>>()
    val upcomingEvent: LiveData<List<ListEventsItem?>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<List<ListEventsItem?>>()
    val finishedEvent: LiveData<List<ListEventsItem?>> = _finishedEvent

    private val _detailEvent = MutableLiveData<Event?>()
    val detailEvent: LiveData<Event?> = _detailEvent

    private val _searchEvent = MutableLiveData<List<ListEventsItem?>>()
    val searchEvent: LiveData<List<ListEventsItem?>> = _searchEvent

    private val _allFavoriteEvents = MutableLiveData<List<FavoriteEvent?>>()
    val allFavoriteEvents: LiveData<List<FavoriteEvent?>> get() = _allFavoriteEvents

    init {
        listUpcomingEvents()
        listFinishedEvents()
        listFavoriteEvents()
    }

    fun listUpcomingEvents() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.getUpcomingEvent()
            _isLoading.value = false
            result.onSuccess {
                _upcomingEvent.value = it
                zeroErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    private fun listFinishedEvents() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.getFinishedEvent()
            _isLoading.value = false
            result.onSuccess {
                _finishedEvent.value = it
                zeroErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.getDetailEvent(id)
            _isLoading.value = false
            result.onSuccess {
                _detailEvent.value = it
                zeroErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.searchEvent(keyword)
            _isLoading.value = false
            result.onSuccess {
                _finishedEvent.value = it
                zeroErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun insertFavoriteEvent(event: FavoriteEvent) {
        viewModelScope.launch {
            val success = eventRepository.insertFavoriteEvent(event)
            if (!success) {
                _errorMessage.value = "Gagal memasukkan ke favorite event, coba lagi"
            }
        }
    }

    fun deleteFavoriteEvent(event: FavoriteEvent) {
        viewModelScope.launch {
            val success = eventRepository.deleteFavoriteEvent(event)
            if (!success) {
                _errorMessage.value = "Gagal menghapus favorite event, coba lagi"
            }
        }
    }

    private fun listFavoriteEvents() {
        _isLoading.value = true
        eventRepository.getAllFavoriteEvent().observeForever { favoriteEvents ->
            Log.d("MainViewModel", "Favorite Events: $favoriteEvents")
            _isLoading.value = false
            _allFavoriteEvents.value = favoriteEvents
            zeroErrorMessage()
        }
    }

    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEvent> {
        return eventRepository.getFavoriteEventById(eventId)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getReminderSettings(): LiveData<Boolean> {
        return pref.getReminderSetting().asLiveData()
    }

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminderActive)
        }
    }

    fun zeroErrorMessage() {
        _errorMessage.value = null
    }
}