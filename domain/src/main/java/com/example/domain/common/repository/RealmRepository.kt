package com.example.domain.common.repository

import com.example.domain.api.model.RequestApi

interface RealmRepository {

    suspend fun getRequestsApi(): List<RequestApi>

    suspend fun getRequestApi(id: String): RequestApi?

    suspend fun createRequestApi(requestApi: RequestApi): Boolean

    suspend fun createRequestApi(listRequestApi: List<RequestApi>): Boolean

    suspend fun updateRequestApi(requestApi: RequestApi): Boolean

    suspend fun deleteRequestApi(requestApi: RequestApi): Boolean

}