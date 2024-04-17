package com.example.domain.test_api.repository

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestPathParam
import com.example.domain.test_api.model.Response

interface TestApiRepository {
    suspend fun request(
        method: MethodRequest,
        url: String,
        pathParams: List<RequestPathParam>? = null,
        body: String? = null,
    ): Response
}