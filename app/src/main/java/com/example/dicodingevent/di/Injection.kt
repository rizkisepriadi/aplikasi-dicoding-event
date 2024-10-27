package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.local.SettingPreferences
import com.example.dicodingevent.data.local.dataStore
import com.example.dicodingevent.data.local.database.FavoriteEventRoomDatabase
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.data.repository.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventRoomDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()
        return EventRepository.getInstance(apiService, dao)
    }

    fun providePreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}