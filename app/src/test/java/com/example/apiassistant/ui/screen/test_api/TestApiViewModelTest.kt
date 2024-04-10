package com.example.apiassistant.ui.screen.test_api

import com.example.apiassistant.utils.MainDispatcherRule
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.api.model.RequestPathParam
import com.example.domain.test_api.model.RequestParams
import com.example.domain.test_api.model.Response
import com.example.domain.test_api.usecase.SendRequestUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class TestApiViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockUseCase = Mockito.mock(SendRequestUseCase::class.java)

    @Test
    fun testGetMethodsRequest() {
        val viewModel = TestApiViewModel(TEST_REQUEST_API, mockUseCase)

        val actual = viewModel.getMethodsRequest()
        Assert.assertArrayEquals(MethodRequest.values(), actual)
    }

    @Test
    fun testOnActionSetUrl() {
        val viewModel = TestApiViewModel(TEST_REQUEST_API, mockUseCase)

        viewModel.onAction(TestApiViewModel.Action.SetUrl("new_test_url"))

        val expectedState = TestApiViewModel.State(
            method = TEST_REQUEST_API.method,
            url = "new_test_url",
            pathParams = TEST_REQUEST_API.pathParams
        )
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    @Test
    fun testOnActionChangeMethodRequest() {
        val viewModel = TestApiViewModel(TEST_REQUEST_API, mockUseCase)

        viewModel.onAction(TestApiViewModel.Action.ChangeMethodRequest(MethodRequest.POST))

        val expectedState = TestApiViewModel.State(
            method = MethodRequest.POST,
            url = TEST_REQUEST_API.url,
            pathParams = TEST_REQUEST_API.pathParams
        )
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    @Test
    fun testOnActionUpdateNamePathVariable() {
        val viewModel = TestApiViewModel(TEST_REQUEST_API, mockUseCase)

        viewModel.onAction(TestApiViewModel.Action.UpdateValuePathVariable(0, "test_value"))

        val expectedState = TestApiViewModel.State(
            method = TEST_REQUEST_API.method,
            url = TEST_REQUEST_API.url,
            pathParams = listOf(
                RequestPathParam(name="name", type="type", value = "test_value")
            )
        )
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    @Test
    fun testOnActionUpdateBodyJson() {
        val viewModel = TestApiViewModel(TEST_REQUEST_API, mockUseCase)

        viewModel.onAction(TestApiViewModel.Action.UpdateBodyJson("test_json"))

        val expectedState = TestApiViewModel.State(
            method = TEST_REQUEST_API.method,
            url = TEST_REQUEST_API.url,
            body = "test_json",
            pathParams = TEST_REQUEST_API.pathParams
        )
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    @Test
    fun testOnActionRequestApi() = runBlocking {
        val viewModel = TestApiViewModel(TEST_REQUEST_API, mockUseCase)
        `when`(mockUseCase.request(
            RequestParams(
            method = viewModel.state.value.method,
            url =  viewModel.state.value.url,
            pathParams =  viewModel.state.value.pathParams,
            body =  viewModel.state.value.body
        )
        )).thenReturn(
            Response(
                code = 200,
                message = "OK",
                body = "test_json"
            )
        )

        viewModel.onAction(TestApiViewModel.Action.RequestApi)

        val expectedState = TestApiViewModel.State(
            method = TEST_REQUEST_API.method,
            url = TEST_REQUEST_API.url,
            pathParams = TEST_REQUEST_API.pathParams,
            response = Response(code = 200, message = "OK", body = "test_json")
        )
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    companion object {
        private val TEST_REQUEST_API = RequestApi(
            id = "1",
            method = MethodRequest.POST,
            url = "test_url",
            pathParams = listOf(
                RequestPathParam(
                    name = "name",
                    type = "type"
                )
            )
        )
    }
}