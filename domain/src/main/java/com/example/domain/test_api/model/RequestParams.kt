package com.example.domain.test_api.model

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestPathParam

data class RequestParams(
    val method: MethodRequest,
    val url: String,
    val pathParams: List<RequestPathParam>? = null,
    val body: String? = null
)