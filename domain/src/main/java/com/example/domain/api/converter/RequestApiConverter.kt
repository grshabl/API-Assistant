package com.example.domain.api.converter

import com.example.domain.api.model.RequestPathParam
import io.swagger.v3.oas.models.parameters.Parameter

fun Parameter.toRequestPathParam() =
    RequestPathParam(
        name = name,
        type = schema.type
    )

fun List<Parameter>?.toListRequestPathParam() =
    this?.map { it.toRequestPathParam() }