package com.example.domain.test_api.repository

import com.example.domain.api.enums.MethodRequest
import com.example.domain.test_api.model.Response

interface TestApiRepository {
    suspend fun request(
        method: MethodRequest,
        url: String,
        body: String? = null
    ): Response
}