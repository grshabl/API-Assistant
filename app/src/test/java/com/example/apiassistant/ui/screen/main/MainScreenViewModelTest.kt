package com.example.apiassistant.ui.screen.main

import com.example.apiassistant.utils.MainDispatcherRule
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.main.usecase.DeleteApiUseCase
import com.example.domain.main.usecase.DetectVoiceCommandUseCase
import com.example.domain.main.usecase.GetApiUseCase
import com.example.domain.main.usecase.LikeApiUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class MainScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockDetectVoiceCommandUseCase = Mockito.mock(DetectVoiceCommandUseCase::class.java)
    private val mockGetApiUseCase = Mockito.mock(GetApiUseCase::class.java)
    private val mockLikeApiUseCase = Mockito.mock(LikeApiUseCase::class.java)
    private val mockDeleteApiUseCase = Mockito.mock(DeleteApiUseCase::class.java)

    @Test
    fun testInitViewModel() = runBlocking {
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        viewModel.updateState()
        val expected = MainScreenViewModel.State(listApi = TEST_LIST_REQUEST_API)
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun testUpdateState() = runBlocking {
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        viewModel.updateState()
        val expected = MainScreenViewModel.State(listApi = TEST_LIST_REQUEST_API)
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun testGetLikesApi() = runBlocking {
        `when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        val actual = viewModel.getLikesApi(viewModel.state.value.listApi)
        val expected = listOf(
            RequestApi(
                id = "2",
                method = MethodRequest.GET,
                url = "test_url_2",
                voiceString = "command",
                isLike = true
            )
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testOnActionRecognizeVoiceCommand_isSuccess() = runBlocking {
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)
        Mockito.`when`(mockDetectVoiceCommandUseCase.detectVoiceCommand("command"))
            .thenReturn(
                RequestApi(
                    id = "2",
                    method = MethodRequest.GET,
                    url = "test_url_2",
                    voiceString = "command",
                    isLike = true
                )
            )
        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )
        val expectedState = MainScreenViewModel.State(
            listApi = TEST_LIST_REQUEST_API
        )
        Assert.assertEquals(expectedState, viewModel.state.value)

        viewModel.onAction(MainScreenViewModel.Action.RecognizeVoiceCommand("command"))

        val expectedEffect: MainScreenViewModel.Effect = MainScreenViewModel.Effect.NavigateToTestApiScreen(
            requestApi = RequestApi(
                id = "2",
                method = MethodRequest.GET,
                url = "test_url_2",
                voiceString = "command",
                isLike = true
            ),
            detectedVoiceCommand = "command"
        )
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionRecognizeVoiceCommand_isFailure() = runBlocking {
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)
        Mockito.`when`(mockDetectVoiceCommandUseCase.detectVoiceCommand("command"))
            .thenReturn(null)
        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )
        val expectedState = MainScreenViewModel.State(
            listApi = TEST_LIST_REQUEST_API
        )
        Assert.assertEquals(expectedState, viewModel.state.value)

        viewModel.onAction(MainScreenViewModel.Action.RecognizeVoiceCommand("command"))

        val expectedEffect: MainScreenViewModel.Effect? = null
        Assert.assertEquals(null, viewModel.effect.value)
    }

    @Test
    fun testOnActionOnClickAddApi() = runBlocking {
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        viewModel.onAction(MainScreenViewModel.Action.OnClickAddApi)
        val expectedState = MainScreenViewModel.State(listApi = TEST_LIST_REQUEST_API)
        val expectedEffect : MainScreenViewModel.Effect? = MainScreenViewModel.Effect.NavigateToAddApiScreen
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionOnClickApi() = runBlocking {
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        viewModel.onAction(MainScreenViewModel.Action.OnClickApi(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        ))
        val expectedState = MainScreenViewModel.State(listApi = TEST_LIST_REQUEST_API)
        val expectedEffect : MainScreenViewModel.Effect? = MainScreenViewModel.Effect.NavigateToTestApiScreen(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        )
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionOnClickLikeApi_isSuccess() = runBlocking {
        Mockito.`when`(mockLikeApiUseCase.likeApi(
            requestApi = RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            ),
            isLike = true
        )).thenReturn(true)
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(listOf(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1",
                isLike = true
            ),
            RequestApi(
                id = "2",
                method = MethodRequest.GET,
                url = "test_url_2",
                voiceString = "command",
                isLike = true
            )
        ))

        viewModel.onAction(MainScreenViewModel.Action.OnClickLikeApi(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        ))
        val expectedState = MainScreenViewModel.State(listApi = listOf(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1",
                isLike = true
            ),
            RequestApi(
                id = "2",
                method = MethodRequest.GET,
                url = "test_url_2",
                voiceString = "command",
                isLike = true
            )
        ))
        val expectedEffect : MainScreenViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionOnClickLikeApi_isFailure() = runBlocking {
        Mockito.`when`(mockLikeApiUseCase.likeApi(
            requestApi = RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            ),
            isLike = true
        )).thenReturn(false)
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        viewModel.onAction(MainScreenViewModel.Action.OnClickLikeApi(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        ))
        val expectedState = MainScreenViewModel.State(listApi = TEST_LIST_REQUEST_API)
        val expectedEffect : MainScreenViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionOnClickDeleteApi_isSuccess() = runBlocking {
        Mockito.`when`(mockDeleteApiUseCase.deleteApi(
            requestApi = RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        )).thenReturn(true)
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(listOf(
            RequestApi(
                id = "2",
                method = MethodRequest.GET,
                url = "test_url_2",
                voiceString = "command",
                isLike = true
            )
        ))

        viewModel.onAction(MainScreenViewModel.Action.OnClickDeleteApi(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        ))
        val expectedState = MainScreenViewModel.State(listApi = listOf(
            RequestApi(
                id = "2",
                method = MethodRequest.GET,
                url = "test_url_2",
                voiceString = "command",
                isLike = true
            )
        ))
        val expectedEffect : MainScreenViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionOnClickDeleteApi_isFailure() = runBlocking {
        Mockito.`when`(mockDeleteApiUseCase.deleteApi(
            requestApi = RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        )).thenReturn(false)
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        viewModel.onAction(MainScreenViewModel.Action.OnClickDeleteApi(
            RequestApi(
                id = "1",
                method = MethodRequest.GET,
                url = "test_url_1"
            )
        ))
        val expectedState = MainScreenViewModel.State(listApi = TEST_LIST_REQUEST_API)
        val expectedEffect : MainScreenViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionNavigateToOtherScreen() = runBlocking {
        Mockito.`when`(mockGetApiUseCase.getAllApi()).thenReturn(TEST_LIST_REQUEST_API)

        val viewModel = MainScreenViewModel(
            mockDetectVoiceCommandUseCase,
            mockGetApiUseCase,
            mockLikeApiUseCase,
            mockDeleteApiUseCase
        )

        viewModel.onAction(MainScreenViewModel.Action.NavigateToOtherScreen)

        val expectedState = MainScreenViewModel.State(listApi = TEST_LIST_REQUEST_API)
        val expectedEffect: MainScreenViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
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
                voiceString = "command",
                isLike = true
            )
        )
    }
}