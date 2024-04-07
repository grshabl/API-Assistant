package com.example.domain.main.usecase

import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class DetectVoiceCommandUseCaseTest {

    private val mockRepository = Mockito.mock(RealmRepository::class.java)

    @Test
    fun testDetectVoiceCommand_notFound() = runBlocking {
        Mockito.`when`(mockRepository.getRequestsApi()).thenReturn(TEST_LIST_REQUEST_API)

        val useCase = DetectVoiceCommandUseCaseImpl(mockRepository)

        val actual = useCase.detectVoiceCommand("this command not found")
        val expected : RequestApi? = null
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testDetectVoiceCommand_foundFullSimilarCommand() = runBlocking {
        Mockito.`when`(mockRepository.getRequestsApi()).thenReturn(TEST_LIST_REQUEST_API)

        val useCase = DetectVoiceCommandUseCaseImpl(mockRepository)

        val actual = useCase.detectVoiceCommand("command 2")
        val expected = RequestApi(
            id = "3",
            method = MethodRequest.GET,
            url = "test_url_3",
            voiceString = "command 2"
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testDetectVoiceCommand_foundSimilarMore80Percent() = runBlocking {
        Mockito.`when`(mockRepository.getRequestsApi()).thenReturn(TEST_LIST_REQUEST_API)

        val useCase = DetectVoiceCommandUseCaseImpl(mockRepository)

        val actual = useCase.detectVoiceCommand("command tost")
        val expected = RequestApi(
            id = "4",
            method = MethodRequest.GET,
            url = "test_url_4",
            voiceString = "command test"
        )
        Assert.assertEquals(expected, actual)
    }

    companion object {
        private val TEST_LIST_REQUEST_API = listOf(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            ),
            RequestApi(
                id = "2",
                method = MethodRequest.GET,
                url = "test_url_2",
                voiceString = "command 1"
            ),
            RequestApi(
                id = "3",
                method = MethodRequest.GET,
                url = "test_url_3",
                voiceString = "command 2"
            ),
            RequestApi(
                id = "4",
                method = MethodRequest.GET,
                url = "test_url_4",
                voiceString = "command test"
            ),
        )
    }
}