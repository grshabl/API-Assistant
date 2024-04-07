package com.example.domain.test_api.usecase

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestPathParam
import com.example.domain.test_api.model.RequestParams
import com.example.domain.test_api.model.Response
import com.example.domain.test_api.repository.TestApiRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class SendRequestUseCaseTest {

    private val mockRepository = Mockito.mock(TestApiRepository::class.java)

    @Test
    fun testRequest_isSuccess() = runBlocking {
        Mockito.`when`(mockRepository.request(
            method = TEST_REQUEST_PARAMS.method,
            url = "test_url?var1=1&var2=2",
            body = TEST_REQUEST_PARAMS.body
        )).thenReturn(
            Response(
                code = 200,
                message = "OK"
            )
        )

        val useCase = SendRequestUseCaseImpl(mockRepository)

        val actual = useCase.request(TEST_REQUEST_PARAMS)
        val expected = Response(
            code = 200,
            message = "OK"
        )
        Assert.assertEquals(expected, actual)
    }

    companion object {
        private val TEST_REQUEST_PARAMS = RequestParams(
            method = MethodRequest.GET,
            url = "test_url",
            pathParams = listOf(
                RequestPathParam(name = "var1", type = "int", value="1"),
                RequestPathParam(name = "var2", type = "int", value="2")
            ),
            body = "{}"
        )
    }
}