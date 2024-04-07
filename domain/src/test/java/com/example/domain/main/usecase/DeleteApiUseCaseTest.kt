package com.example.domain.main.usecase

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class DeleteApiUseCaseTest {

    private val mockRepository = Mockito.mock(RealmRepository::class.java)

    @Test
    fun testDeleteApi_isCorrect() = runBlocking {
        Mockito.`when`(mockRepository.deleteRequestApi(TEST_REQUEST_API)).thenReturn(true)

        val useCase = DeleteApiUseCaseImpl(mockRepository)

        val actual = useCase.deleteApi(TEST_REQUEST_API)
        val expected = true
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testDeleteApi_isFailed() = runBlocking {
        Mockito.`when`(mockRepository.deleteRequestApi(TEST_REQUEST_API)).thenReturn(false)

        val useCase = DeleteApiUseCaseImpl(mockRepository)

        val actual = useCase.deleteApi(TEST_REQUEST_API)
        val expected = false
        Assert.assertEquals(expected, actual)
    }

    companion object {
        private val TEST_REQUEST_API = RequestApi(
            method = MethodRequest.GET,
            url = "test_url"
        )
    }
}