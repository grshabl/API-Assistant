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
    @Assisted private val requestApi: RequestApi,
    private val sendRequestUseCase: SendRequestUseCase
) : ViewModel() {
    private val _state: MutableState<State> = mutableStateOf(State(
        method = requestApi.method,
        url = requestApi.url,
        pathParams = requestApi.pathParams,
        body = requestApi.body
    ))
    val state: androidx.compose.runtime.State<State> = _state

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
                value = value ?: listNewPathParams[index].type
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

    sealed class Action {
        data class SetUrl(val url: String): Action()
        data class ChangeMethodRequest(val methodRequest: MethodRequest): Action()
        data class UpdateValuePathVariable(val index: Int, val value: String): Action()
        data class UpdateBodyJson(val json: String): Action()
        data object RequestApi: Action()
    }

    data class State(
        var isLoading: Boolean = false,
        var method: MethodRequest = MethodRequest.GET,
        var url: String = "",
        var pathParams: List<RequestPathParam>? = null,
        var body: String? = null,
        var response: Response? = null
    )

    @AssistedFactory
    interface TestApiViewModelFactory {
        fun create(requestApi: RequestApi): TestApiViewModel
    }
}