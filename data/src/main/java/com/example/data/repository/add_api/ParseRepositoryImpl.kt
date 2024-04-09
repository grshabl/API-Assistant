package com.example.data.repository.add_api

import com.example.data.source.api.Network
import com.example.domain.add_api.repository.ParseRepository
import com.example.domain.api.model.RequestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParseRepositoryImpl @Inject constructor() : ParseRepository {

    override suspend fun parse(url: String): List<RequestApi>?  = withContext(Dispatchers.IO) {
        return@withContext Network.apiService.parse(url).body()
    }

}