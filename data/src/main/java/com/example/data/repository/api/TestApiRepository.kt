package com.example.data.repository.api

import com.example.domain.api.enums.MethodRequest
import com.example.domain.test_api.model.Response
import com.example.domain.test_api.repository.TestApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class TestApiRepositoryImpl @Inject constructor(): TestApiRepository {

    override suspend fun request(
        method: MethodRequest,
        url: String,
        body: String?
    ) = withContext(Dispatchers.IO) {
        //may be in viewModel
        val client = OkHttpClient()

        val mediaType =  MEDIA_TYPE_BODY.toMediaType()
        val requestBody = body?.toRequestBody(mediaType)

        val request = if (requestBody != null)
            Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        else
            Request.Builder()
                .url(url)
                .build()

        val response = client.newCall(request).execute()

        return@withContext Response(
            code = response.code,
            message = response.message,
            body = response.body?.string()
        )
    }

    companion object {
        private val MEDIA_TYPE_BODY = "application/json; charset=utf-8"
    }

}