package com.example.domain.main.usecase

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class GetApiUseCaseTest {

    private val mockRepository = Mockito.mock(RealmRepository::class.java)

    @Test
    fun testGetAllApi_fullList() = runBlocking {
        Mockito.`when`(mockRepository.getRequestsApi()).thenReturn(TEST_LIST_REQUEST_API)

        val useCase = GetApiUseCaseImpl(mockRepository)

        val actual = useCase.getAllApi()
        val expected = TEST_LIST_REQUEST_API
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testGetAllApi_emptyList() = runBlocking {
        Mockito.`when`(mockRepository.getRequestsApi()).thenReturn(emptyList())

        val useCase = GetApiUseCaseImpl(mockRepository)

        val actual = useCase.getAllApi()
        val expected = emptyList<RequestApi>()
        Assert.assertEquals(expected, actual)
    }

    companion object {
        private val TEST_LIST_REQUEST_API = listOf(
            RequestApi(
                method = MethodRequest.GET,
                url = "test_url_1"
            ),
            RequestApi(
                method = MethodRequest.GET,
                url = "test_url_2"
            )
        )
    }
}