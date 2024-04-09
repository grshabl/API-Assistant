package com.example.domain.add_api.usecase

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class AddApiUseCaseTest {

    private val mockRepository = mock(RealmRepository::class.java)

    @Test
    fun testAddApi_isCorrect() = runBlocking {
        `when`(mockRepository.createRequestApi(TEST_REQUEST_API)).thenReturn(true)

        val useCase = AddApiUseCaseImpl(mockRepository)

        val actual = useCase.addApi(TEST_REQUEST_API)
        val expected = true
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testAddApi_isFailed() = runBlocking {
        `when`(mockRepository.createRequestApi(TEST_REQUEST_API)).thenReturn(false)

        val useCase = AddApiUseCaseImpl(mockRepository)

        val actual = useCase.addApi(TEST_REQUEST_API)
        val expected = false
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testAddListApi_isCorrect() = runBlocking {
        `when`(mockRepository.createRequestApi(listOf(TEST_REQUEST_API))).thenReturn(true)

        val useCase = AddApiUseCaseImpl(mockRepository)

        val actual = useCase.addApi(listOf(TEST_REQUEST_API))
        val expected = true
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testAddListApi_isFailed() = runBlocking {
        `when`(mockRepository.createRequestApi(listOf(TEST_REQUEST_API))).thenReturn(false)

        val useCase = AddApiUseCaseImpl(mockRepository)

        val actual = useCase.addApi(listOf(TEST_REQUEST_API))
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