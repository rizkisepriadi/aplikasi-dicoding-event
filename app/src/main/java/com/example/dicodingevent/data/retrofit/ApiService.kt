package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.DetailResponse
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getListEvents(
        @Query("active") active: Int,
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailResponse<Event>>

    @GET("events")
    fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String
    ): Call<EventResponse>
}