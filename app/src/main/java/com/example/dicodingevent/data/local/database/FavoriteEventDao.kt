package com.example.dicodingevent.data.local.database

import android.hardware.TriggerEvent
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingevent.data.local.model.FavoriteEvent

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteEvent(event: FavoriteEvent): Long

    @Delete
    suspend fun deleteFavoriteEvent(event: FavoriteEvent)

    @Query("SELECT * from favorite_event")
    fun getAllFavoriteEvent(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_event WHERE event_id = :eventId")
    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEvent>
}