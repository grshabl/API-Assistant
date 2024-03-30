package com.example.domain.common.repository

import com.example.domain.api.model.RequestApi

interface RealmRepository {

    suspend fun getRequestsApi(): List<RequestApi>

    suspend fun getRequestApi(id: String): RequestApi?

    fun createRequestApi(requestApi: RequestApi)

    fun updateRequestApi(requestApi: RequestApi)

    fun deleteRequestApi(requestApi: RequestApi)

}