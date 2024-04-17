package com.example.apiassistant.ui.screen.add_api

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.add_api.usecase.AddApiUseCase
import com.example.domain.add_api.usecase.ParseUseCase
import com.example.domain.add_api.usecase.UpdateApiUseCase
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.example.domain.api.model.RequestPathParam
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel(assistedFactory = AddApiViewModel.AddApiViewModelFactory::class)
class AddApiViewModel @AssistedInject constructor(
    @Assisted val requestApi: RequestApi? = null,
    private val addApiUseCase: AddApiUseCase,
    private val updateApiUseCase: UpdateApiUseCase,
    private val parseUseCase: ParseUseCase
) : ViewModel() {

    private val _state: MutableState<State> = mutableStateOf(State(
        id = requestApi?.id,
        method = requestApi?.method ?: MethodRequest.GET,
        url = requestApi?.url ?: "",
        pathParams = requestApi?.pathParams,
        body = requestApi?.body,
        voiceString = requestApi?.voiceString,
    ))
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
            try {
                _state.value = state.value.copy(isLoading = true)
                val listParseRequestApi = parseUseCase.parse(url)
                if (listParseRequestApi != null) {
                    addApiUseCase.addApi(listParseRequestApi)
                    _effect.value = Effect.GoBack
                } else {
                    _effect.value = Effect.ShowErrorParse
                }
            } catch (e : Exception) {
                _effect.value = Effect.ShowErrorParse
            } finally {
                _state.value = state.value.copy(isLoading = false)
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
                type = type ?: listNewPathParams[index].type
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
            if (state.value.id == null) {
                addApiUseCase.addApi(requestApi)
            } else {
                updateApiUseCase.updateApi(requestApi)
            }

            _effect.value = Effect.GoBack
        }
    }

    fun getMethodsRequest() = MethodRequest.values()

    fun onAction(action: Action) {
        when (action) {
            is Action.SetUrl -> { setUrl(action.url) }
            is Action.ChangeMethodRequest -> { setMethodRequest(method = action.methodRequest) }
            is Action.ParseSwaggerApi -> { parseSwaggerApi(url = action.urlSwagger) }
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
                        id = state.value.id ?: UUID.randomUUID().toString(),
                        method = state.value.method,
                        url = state.value.url,
                        pathParams = state.value.pathParams,
                        body = state.value.body,
                        voiceString = state.value.voiceString,
                        isLike = requestApi?.isLike ?: false
                    )
                )
            }
        }
    }

    sealed class Action {
        data class SetUrl(val url: String): Action()
        data class ChangeMethodRequest(val methodRequest: MethodRequest): Action()
        data class ParseSwaggerApi(val urlSwagger: String): Action()
        data class UpdateNamePathVariable(val index: Int, val name: String): Action()
        data class UpdateTypePathVariable(val index: Int, val value: String): Action()
        data object AddPathVariable: Action()
        data class UpdateBodyJson(val json: String): Action()
        data class UpdateVoiceCommand(val voiceText: String): Action()
        data object SaveApi: Action()
    }

    data class State(
        var isLoading: Boolean = false,
        var id: String? = null,
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

    @AssistedFactory
    interface AddApiViewModelFactory {
        fun create(requestApi: RequestApi? = null): AddApiViewModel
    }
}