package com.example.dicodingevent.data.remote.retrofit

import com.example.dicodingevent.data.remote.response.DetailResponse
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.data.remote.response.EventResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("events")
    suspend fun getAllActiveEvent(
        @Query("active") active: Int = 1,
    ): Response<EventResponse>

    @GET("events")
    suspend fun getAllFinishedEvent(
        @Query("active") active: Int = 0,
    ): Response<EventResponse>

    @GET("events")
    suspend fun searchEvents(
        @Query("q") keyword: String,
        @Query("active") active: Int = -1,
    ): Response<EventResponse>

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): Response<DetailResponse<Event?>>

}