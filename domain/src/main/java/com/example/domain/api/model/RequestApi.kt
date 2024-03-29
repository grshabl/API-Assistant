package com.example.domain.api.model

import com.example.domain.api.enums.MethodRequest

data class RequestApi(
    val method: MethodRequest,
    val url: String,
    val pathParams: List<RequestPathParam>? = null,
    val body: String? = null,
    val voiceString: String? = null,
    val isLike: Boolean = false
)