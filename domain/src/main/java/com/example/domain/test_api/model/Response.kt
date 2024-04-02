package com.example.domain.test_api.model

data class Response(
    val code: Int,
    val message: String, // ???
    val body: String? = null
)