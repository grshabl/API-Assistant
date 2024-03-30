package com.example.apiassistant.ui.screen.test_api

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.api.model.RequestPathParam
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = TestApiViewModel.TestApiViewModelFactory::class)
class TestApiViewModel @AssistedInject constructor(
    @Assisted private val requestApi: RequestApi
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

    private fun requestApi(
        method: MethodRequest,
        url: String,
        pathParams: List<RequestPathParam>?,
        body: String?
    ) {
        val response = "SUCCESS"
        // ...
        _state.value = state.value.copy(
            response = response
        )
    }

    fun getMethodsRequest() = MethodRequest.values()

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
                    method = state.value.method,
                    url = state.value.url,
                    pathParams = state.value.pathParams,
                    body = state.value.body
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
        var method: MethodRequest = MethodRequest.GET,
        var url: String = "",
        var pathParams: List<RequestPathParam>? = null,
        var body: String? = null,
        var response: String = ""
    )

    @AssistedFactory
    interface TestApiViewModelFactory {
        fun create(requestApi: RequestApi): TestApiViewModel
    }
}