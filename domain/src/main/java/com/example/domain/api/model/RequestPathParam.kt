package com.example.domain.api.model

data class RequestPathParam(
    val name: String,
    val type: String, //value - значение из IntegerSchema.getType()
    val value: String = ""
)