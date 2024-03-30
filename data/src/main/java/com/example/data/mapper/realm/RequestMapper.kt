package com.example.data.mapper.realm

import com.example.data.source.realm.entity.Request
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi

fun Request.mapTo() =
    RequestApi(
        id = id,
        method = method.mapTo(),
        url = url,
        pathParams = pathParams?.toList()?.map{ it.mapTo() },
        body = body,
        voiceString = voiceString,
        isLike = isLike
    )

fun List<Request>.mapTo() =
    this.map { it.mapTo() }

private fun String.mapTo(): MethodRequest =
    when(this) {
        MethodRequest.GET.name -> MethodRequest.GET
        MethodRequest.POST.name -> MethodRequest.POST
        MethodRequest.DELETE.name -> MethodRequest.DELETE
        MethodRequest.PUT.name -> MethodRequest.PUT
        MethodRequest.HEAD.name -> MethodRequest.HEAD
        MethodRequest.PATCH.name -> MethodRequest.PATCH
        else -> MethodRequest.GET
    }