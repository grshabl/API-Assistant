package com.example.data.mapper.realm

import com.example.data.source.realm.entity.RequestPathParam

fun RequestPathParam.mapTo() =
    com.example.domain.api.model.RequestPathParam(
        name = name ?: "",
        type = type ?: "",
        value = value ?: ""
    )

fun com.example.domain.api.model.RequestPathParam.mapTo() =
    RequestPathParam(
        name = name,
        type = type,
        value = value ?: ""
    )