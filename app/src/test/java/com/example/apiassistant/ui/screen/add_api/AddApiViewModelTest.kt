package com.example.apiassistant.ui.screen.add_api

import com.example.apiassistant.utils.MainDispatcherRule
import com.example.domain.add_api.usecase.AddApiUseCase
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.api.model.RequestPathParam
import com.example.domain.api.usecase.ParserUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException

class AddApiViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockAddApiUseCase = Mockito.mock(AddApiUseCase::class.java)
    private val mockParserUseCase = Mockito.mock(ParserUseCase::class.java)

    @Test
    fun testSetDefaultEffect() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.setDefaultEffect()
        Assert.assertEquals(null, viewModel.effect.value)
    }

    @Test
    fun testGetMethodsRequest() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        val actual = viewModel.getMethodsRequest()
        Assert.assertArrayEquals(MethodRequest.values(), actual)
    }

    @Test
    fun testOnActionSetUrl() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.SetUrl("test_url"))

        val expectedState = AddApiViewModel.State(url = "test_url")
        val expectedEffect: AddApiViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionChangeMethodRequest() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.ChangeMethodRequest(MethodRequest.POST))

        val expectedState = AddApiViewModel.State(method = MethodRequest.POST)
        val expectedEffect: AddApiViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionParseSwaggerApi_isSuccess() = runBlocking {
        Mockito.`when`(mockParserUseCase.getListRequestApi("https://petstore.swagger.io/v2/swagger.json"))
            .thenReturn(listOf(
                RequestApi(
                    id = "1",
                    method = MethodRequest.GET,
                    url = "test_url"
                )
            ))
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.ParseSwaggerApi("https://petstore.swagger.io/v2/swagger.json"))

        val expectedState = AddApiViewModel.State()
        val expectedEffect: AddApiViewModel.Effect = AddApiViewModel.Effect.GoBack
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionParseSwaggerApi_isFailed() = runBlocking {
        Mockito.`when`(mockParserUseCase.getListRequestApi("https://petstore.swagger.io/v2/swagger.json"))
            .thenThrow(MockitoException("Error parse"))
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.ParseSwaggerApi("https://petstore.swagger.io/v2/swagger.json"))

        val expectedState = AddApiViewModel.State()
        val expectedEffect: AddApiViewModel.Effect = AddApiViewModel.Effect.ShowErrorParse
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionUpdateNamePathVariable() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.AddPathVariable)
        viewModel.onAction(AddApiViewModel.Action.UpdateNamePathVariable(0, "test_name"))

        val expectedState = AddApiViewModel.State(pathParams = listOf(RequestPathParam(name="test_name", type="")))
        val expectedEffect: AddApiViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionUpdateTypePathVariable() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.AddPathVariable)
        viewModel.onAction(AddApiViewModel.Action.UpdateTypePathVariable(0, "test_type"))

        val expectedState = AddApiViewModel.State(pathParams = listOf(RequestPathParam(name="", type="test_type")))
        val expectedEffect: AddApiViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionAddPathVariable() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.AddPathVariable)

        val expectedState = AddApiViewModel.State(pathParams = listOf(RequestPathParam(name="", type="")))
        val expectedEffect: AddApiViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionUpdateBodyJson() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.UpdateBodyJson("test_json"))

        val expectedState = AddApiViewModel.State(body = "test_json")
        val expectedEffect: AddApiViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionUpdateVoiceCommand() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.UpdateVoiceCommand("test_command"))

        val expectedState = AddApiViewModel.State(voiceString = "test_command")
        val expectedEffect: AddApiViewModel.Effect? = null
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }

    @Test
    fun testOnActionSaveApi() {
        val viewModel = AddApiViewModel(mockAddApiUseCase, mockParserUseCase)

        viewModel.onAction(AddApiViewModel.Action.SaveApi)

        val expectedState = AddApiViewModel.State()
        val expectedEffect: AddApiViewModel.Effect = AddApiViewModel.Effect.GoBack
        Assert.assertEquals(expectedState, viewModel.state.value)
        Assert.assertEquals(expectedEffect, viewModel.effect.value)
    }
}