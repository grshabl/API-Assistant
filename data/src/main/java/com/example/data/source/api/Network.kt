package com.example.data.source.api

import com.example.data.config.ConfigApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {

    private val okHttpClient = OkHttpClient.Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ConfigApi.URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    val apiService = retrofit.create(ApiService::class.java)

}