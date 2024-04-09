package com.example.data.source.api

import com.example.domain.api.model.RequestApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("parse")
    suspend fun parse(@Body url: String): Response<List<RequestApi>>

}