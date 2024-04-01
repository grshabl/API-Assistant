package com.example.apiassistant.ui.screen.add_api

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.add_api.AddApiUseCase
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.api.model.RequestPathParam
import com.example.domain.api.usecase.ParserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddApiViewModel @Inject constructor(
    private val addApiUseCase: AddApiUseCase,
    private val parserUseCase: ParserUseCase
) : ViewModel() {

    private val _state: MutableState<State> = mutableStateOf(State())
    val state: androidx.compose.runtime.State<State> = _state

    private val _effect: MutableState<Effect?> = mutableStateOf(null)
    val effect: androidx.compose.runtime.State<Effect?> = _effect

    private fun setUrl(url: String) {
        _state.value = state.value.copy(url = url)
    }

    private fun setMethodRequest(method: MethodRequest) {
        _state.value = state.value.copy(method = method)
    }

    private fun parseSwaggerApi(url: String) {
        viewModelScope.launch {
            // D:\Fast projects\testParserSwagger\swaggerParserKotlin\src\main\resources\swagger_example.json
            val url = "https://petstore.swagger.io/v2/swagger.json"
            try {
                val listParseRequestApi = parserUseCase.getListRequestApi(url)
                addApiUseCase.addApi(listParseRequestApi)
                _effect.value = Effect.GoBack
            } catch (e : Exception) {
                _effect.value = Effect.ShowErrorParse
            }
        }
    }

    fun setDefaultEffect() {
        _effect.value = null
    }

    @Throws(IndexOutOfBoundsException::class)
    private fun updatePathVariable(name: String? = null, type: String? = null, index: Int) {
        if (state.value.pathParams == null || state.value.pathParams!!.size <= index) {
            throw IndexOutOfBoundsException("invalid index")
        }

        val listNewPathParams = state.value.pathParams?.toMutableList() ?: mutableListOf()
        listNewPathParams[index] = listNewPathParams[index]
            .copy(
                name = name ?: listNewPathParams[index].name,
                value = type ?: listNewPathParams[index].type
            )
        _state.value = _state.value.copy(pathParams = listNewPathParams)
    }

    private fun addPathVariable() {
        val lastParams = (_state.value.pathParams?.toMutableList() ?: mutableListOf())
        lastParams.add(
            RequestPathParam(name = "", type = "")
        )
        _state.value = _state.value.copy(pathParams = lastParams)
    }

    private fun setBody(bodyJson: String) {
        _state.value = _state.value.copy(body = bodyJson)
    }

    private fun setVoiceCommand(voiceString: String?) {
        _state.value = state.value.copy(voiceString = voiceString)
    }

    private fun saveApi(requestApi: RequestApi) {
        viewModelScope.launch {
            addApiUseCase.addApi(requestApi)
            _effect.value = Effect.GoBack
        }
    }

    fun getMethodsRequest() = MethodRequest.values()

    fun onAction(action: Action) {
        when (action) {
            is Action.SetUrl -> { setUrl(action.url) }
            is Action.ChangeMethodRequest -> { setMethodRequest(method = action.methodRequest) }
            Action.ParseSwaggerApi -> { parseSwaggerApi(url = state.value.url) }
            is Action.UpdateNamePathVariable -> {
                updatePathVariable(name = action.name, index = action.index)
            }
            is Action.UpdateTypePathVariable -> {
                updatePathVariable(type = action.value, index = action.index)
            }
            Action.AddPathVariable -> { addPathVariable() }
            is Action.UpdateBodyJson -> { setBody(bodyJson = action.json) }
            is Action.UpdateVoiceCommand -> { setVoiceCommand(voiceString = action.voiceText)}
            Action.SaveApi -> {
                saveApi(
                    RequestApi(
                        method = state.value.method,
                        url = state.value.url,
                        pathParams = state.value.pathParams,
                        body = state.value.body,
                        voiceString = state.value.voiceString
                    )
                )
            }
        }
    }

    sealed class Action {
        data class SetUrl(val url: String): Action()
        data class ChangeMethodRequest(val methodRequest: MethodRequest): Action()
        data object ParseSwaggerApi: Action()
        data class UpdateNamePathVariable(val index: Int, val name: String): Action()
        data class UpdateTypePathVariable(val index: Int, val value: String): Action()
        data object AddPathVariable: Action()
        data class UpdateBodyJson(val json: String): Action()
        data class UpdateVoiceCommand(val voiceText: String): Action()
        data object SaveApi: Action()
    }

    data class State(
        var method: MethodRequest = MethodRequest.GET,
        var url: String = "",
        var pathParams: List<RequestPathParam>? = null,
        var body: String? = null,
        var voiceString: String? = null,
    )

    sealed class Effect {
        data object GoBack : Effect()
        data object ShowErrorParse: Effect()
    }
}