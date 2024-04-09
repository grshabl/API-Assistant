package com.example.domain.add_api.repository

import com.example.domain.api.model.RequestApi

interface ParseRepository {
    suspend fun parse(url: String): List<RequestApi>?
}