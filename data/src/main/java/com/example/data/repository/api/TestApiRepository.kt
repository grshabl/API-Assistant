package com.example.data.repository.api

import android.util.Log
import com.example.domain.api.enums.MethodRequest
import com.example.domain.test_api.model.Response
import com.example.domain.test_api.repository.TestApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class TestApiRepositoryImpl @Inject constructor(): TestApiRepository {

    override suspend fun request(
        method: MethodRequest,
        url: String,
        pathParams: List<com.example.domain.api.model.RequestPathParam>?,
        body: String?
    ): Response = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val mediaType =  MEDIA_TYPE_BODY.toMediaType()
        val requestBody = body?.toRequestBody(mediaType)

        Log.d("test", "url = $url")

        val httpBuilder: HttpUrl.Builder?  = url.toHttpUrlOrNull()?.newBuilder()
        pathParams?.let {
            for (pathParam in it) {
                httpBuilder?.addQueryParameter(pathParam.name, pathParam.value)
            }
        }
        val newUrl: String = httpBuilder?.build().toString()

        val requestBuilder = Request.Builder().url(newUrl)

        requestBody?.let {
            when (method) {
                MethodRequest.GET -> requestBuilder.get()
                MethodRequest.POST -> requestBuilder.post(requestBody)
                MethodRequest.HEAD -> requestBuilder.head()
                MethodRequest.PUT -> requestBuilder.put(requestBody)
                MethodRequest.PATCH -> requestBuilder.patch(requestBody)
                MethodRequest.DELETE -> requestBuilder.patch(requestBody)
            }
        }

        val request = requestBuilder.build()

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