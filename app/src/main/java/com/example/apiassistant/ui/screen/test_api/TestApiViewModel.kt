package com.example.apiassistant.ui.screen.test_api

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.api.model.RequestPathParam
import com.example.domain.test_api.model.RequestParams
import com.example.domain.test_api.model.Response
import com.example.domain.test_api.usecase.SendRequestUseCase
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TestApiViewModel.TestApiViewModelFactory::class)
class TestApiViewModel @AssistedInject constructor(
    @Assisted("requestApi") val requestApi: RequestApi,
    @Assisted("detectedVoiceCommand") val detectedVoiceCommand: String? = null,
    private val sendRequestUseCase: SendRequestUseCase
) : ViewModel() {
    private val _state: MutableState<State> = mutableStateOf(State(
        method = requestApi.method,
        url = requestApi.url,
        pathParams = requestApi.pathParams,
        body = requestApi.body
    ))
    val state: androidx.compose.runtime.State<State> = _state

    private val _effect: MutableState<Effect?> = mutableStateOf(null)
    val effect: androidx.compose.runtime.State<Effect?> = _effect

    init {
        if (!detectedVoiceCommand.isNullOrEmpty() && requestApi.voiceString != null) {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val mapOfVoiceParams =
                    getMapOfVoiceParams(requestApi.voiceString!!, detectedVoiceCommand)
                val updatedPathParams = getUpdatedPathParams(requestApi.pathParams, mapOfVoiceParams)
                val updatedBody = getUpdateBody(requestApi.body, mapOfVoiceParams)

                _state.value = _state.value.copy(
                    isLoading = false,
                    pathParams = updatedPathParams,
                    body = updatedBody
                )

                onAction(Action.RequestApi)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = _state.value.copy(isLoading = false)
                _effect.value = Effect.ShowToast
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getMapOfVoiceParams(mask: String, maskedString: String): Map<String, String> {
        val pattern = Regex("\\{(int|str|bool)_\\w+\\}")
        val placeholders = pattern.findAll(mask).map { it.value }.toList()

        println(placeholders)

        val wordsMask = mask.split(" ").toMutableList()
        val wordsMaskedText = maskedString.split(" ").toMutableList()

        for (text in wordsMask) {
            wordsMaskedText.remove(text)
        }

        if (wordsMaskedText.size != placeholders.size) {
            throw IllegalArgumentException("couldn't detect voice command")
        }


        return placeholders.zip(wordsMaskedText).toMap()
    }

    private fun getUpdatedPathParams(
        pathParams: List<RequestPathParam>?,
        mapValues: Map<String, String>
    ): List<RequestPathParam>? {
        if (pathParams.isNullOrEmpty() || mapValues.isEmpty()) return pathParams

        val updatePathParams = mutableListOf<RequestPathParam>()
        for (pathParam in pathParams) {
            val entry = mapValues.entries.firstOrNull { it.key.contains(pathParam.name) }

            updatePathParams.add(
                RequestPathParam(
                    name = pathParam.name,
                    type = pathParam.type,
                    value = entry?.value ?: ""
                )
            )
        }

        return updatePathParams
    }

    private fun getUpdateBody(body: String?, mapValues: Map<String, String>) : String? {
        if (body.isNullOrEmpty() || mapValues.isEmpty()) return body

        var newBody = body
        for ((key, value) in mapValues) {
            println("* $key $value $newBody")
            if (listOf("int", "bool").any { key.contains(it) }) {
                newBody = newBody?.replace(key, value)
            } else {
                newBody = newBody?.replace(key, "\"$value\"")
            }
        }

        return newBody
    }

    fun resetEffect() {
        _effect.value = null
    }

    private fun setUrl(url: String) {
        _state.value = state.value.copy(url = url)
    }

    private fun setMethodRequest(method: MethodRequest) {
        _state.value = state.value.copy(method = method)
    }

    @Throws(IndexOutOfBoundsException::class)
    private fun updateValuePathVariable(value: String? = null, index: Int) {
        if (state.value.pathParams == null || state.value.pathParams!!.size <= index) {
            throw IndexOutOfBoundsException("invalid index")
        }

        val listNewPathParams = state.value.pathParams?.toMutableList() ?: mutableListOf()
        listNewPathParams[index] = listNewPathParams[index]
            .copy(
                value = value ?: listNewPathParams[index].value
            )
        _state.value = _state.value.copy(pathParams = listNewPathParams)
    }

    private fun setBody(bodyJson: String) {
        _state.value = _state.value.copy(body = bodyJson)
    }

    private fun requestApi(requestParams: RequestParams) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            var response = Response(
                code = -1,
                message = "Fialed request"
            )

            try {
                response = sendRequestUseCase.request(requestParams)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            _state.value = state.value.copy(
                response = response,
                isLoading = false
            )
        }
    }

    fun getMethodsRequest() = MethodRequest.values()

    fun getResponseText(response: Response?) : String {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonElement = JsonParser.parseString(response?.body)
            val json = gson.toJson(jsonElement)

            return "${response?.code ?: ""} ${response?.message ?: ""}\n${json}"
        } catch (e: Exception) {
            e.printStackTrace()
            return "${response?.code ?: ""} ${response?.message ?: ""}\n${response?.body ?: ""}"
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.SetUrl -> { setUrl(action.url) }
            is Action.ChangeMethodRequest -> { setMethodRequest(method = action.methodRequest) }
            is Action.UpdateValuePathVariable -> {
                updateValuePathVariable(value = action.value, index = action.index)
            }
            is Action.UpdateBodyJson -> { setBody(bodyJson = action.json) }
            Action.RequestApi -> {
                requestApi(
                    RequestParams(
                        method = state.value.method,
                        url = state.value.url,
                        pathParams = state.value.pathParams,
                        body = state.value.body
                    )
                )
            }
        }
    }

    data class State(
        var isLoading: Boolean = false,
        var method: MethodRequest = MethodRequest.GET,
        var url: String = "",
        var pathParams: List<RequestPathParam>? = null,
        var body: String? = null,
        var response: Response? = null
    )

    sealed class Action {
        data class SetUrl(val url: String): Action()
        data class ChangeMethodRequest(val methodRequest: MethodRequest): Action()
        data class UpdateValuePathVariable(val index: Int, val value: String): Action()
        data class UpdateBodyJson(val json: String): Action()
        data object RequestApi: Action()
    }

    sealed class Effect {
        data object ShowToast: Effect()
    }

    @AssistedFactory
    interface TestApiViewModelFactory {
        fun create(
            @Assisted("requestApi") requestApi: RequestApi,
            @Assisted("detectedVoiceCommand") detectedVoiceCommand: String? = null
        ): TestApiViewModel
    }
}