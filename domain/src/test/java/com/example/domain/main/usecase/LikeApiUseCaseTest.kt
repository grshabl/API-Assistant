package com.example.domain.main.usecase

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class LikeApiUseCaseTest {

    private val mockRepository = Mockito.mock(RealmRepository::class.java)

    @Test
    fun testLikeApi_isCorrect() = runBlocking {
        Mockito.`when`(mockRepository.updateRequestApi(TEST_REQUEST_API)).thenReturn(true)

        val useCase = LikeApiUseCaseImpl(mockRepository)

        val actual = useCase.likeApi(TEST_REQUEST_API, true)
        val expected = true
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testLikeApi_isFailed() = runBlocking {
        Mockito.`when`(mockRepository.updateRequestApi(TEST_REQUEST_API)).thenReturn(false)

        val useCase = LikeApiUseCaseImpl(mockRepository)

        val actual = useCase.likeApi(TEST_REQUEST_API, true)
        val expected = false
        Assert.assertEquals(expected, actual)
    }

    companion object {
        private val TEST_REQUEST_API = RequestApi(
            method = MethodRequest.GET,
            url = "test_url",
            isLike = true
        )
    }
}