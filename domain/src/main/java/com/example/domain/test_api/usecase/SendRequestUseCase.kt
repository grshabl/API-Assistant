package com.example.domain.test_api.usecase

import com.example.domain.test_api.model.RequestParams
import com.example.domain.test_api.model.Response
import com.example.domain.test_api.repository.TestApiRepository
import javax.inject.Inject

interface SendRequestUseCase {
    suspend fun request(requestParams: RequestParams): Response
}

class SendRequestUseCaseImpl @Inject constructor(
    private val testApiRepository: TestApiRepository
): SendRequestUseCase {

    override suspend fun request(requestParams: RequestParams): Response {
        var urlWithParams = if (requestParams.pathParams.isNullOrEmpty() ||
            requestParams.url.contains("?")) requestParams.url
        else "${requestParams.url}?"

        requestParams.pathParams?.let {
            for (param in it) {
                urlWithParams += "${param.name}=${param.value}&"
            }
            urlWithParams = urlWithParams.removeSuffix("&")
        }

        val response = testApiRepository.request(
            method = requestParams.method,
            url = urlWithParams,
            body = requestParams.body
        )

        return response
    }

}