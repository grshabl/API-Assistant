package com.example.apiassistant.ui.screen.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableState<State> = mutableStateOf(State())
    val state: androidx.compose.runtime.State<State> = _state

    private val _effect: MutableState<Effect?> = mutableStateOf(null)
    val effect: androidx.compose.runtime.State<Effect?> = _effect

    init {
        viewModelScope.launch {
            _state.value = State(isLoading = true)
            val listApi = listOf(
                RequestApi(method = MethodRequest.GET, url="www.site1.ru/{id}"),
                RequestApi(method = MethodRequest.POST, url="www.site2.ru/", isLike = true),
                RequestApi(method = MethodRequest.GET, url="www.site3.ru/{id}"),
            )
            delay(3000)
            _state.value = _state.value.copy(isLoading = false, listApi = listApi)
            val listApi2 = listOf(
                RequestApi(method = MethodRequest.GET, url="www.site1.ru/{id}"),
                RequestApi(method = MethodRequest.POST, url="www.site2.ru/", isLike = true),
                RequestApi(method = MethodRequest.GET, url="www.site3.ru/{id}"),
                RequestApi(method = MethodRequest.GET, url="www.site4.ru/{id}", isLike = true),
            )
            delay(2000)
            _state.value = _state.value.copy(isLoading = false, listApi = listApi2)
            getLikesApi(listApi)
        }
    }

    private fun resetEffect() {
        _effect.value = null
    }

    fun getLikesApi(listApi: List<RequestApi>) =
        listApi.filter { it.isLike }

    private fun onClickAddApi() {
        _effect.value = Effect.NavigateToAddApiScreen
    }

    private fun onClickApi(requestApi: RequestApi) {
        _effect.value = Effect.NavigateToTestApiScreen
    }
    private fun onClickLikeApi(requestApi: RequestApi) {

    }
    private fun onClickDeleteApi(requestApi: RequestApi) {

    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnClickAddApi -> { onClickAddApi() }
            is Action.OnClickApi -> { onClickApi(requestApi = action.requestApi) }
            is Action.OnClickLikeApi -> { onClickLikeApi(requestApi = action.requestApi) }
            is Action.OnClickDeleteApi -> { onClickDeleteApi(requestApi = action.requestApi) }
            Action.NavigateToOtherScreen -> { resetEffect() }
        }
    }


    sealed class Action {
        data object OnClickAddApi: Action()
        data class OnClickApi(val requestApi: RequestApi): Action()
        data class OnClickDeleteApi(val requestApi: RequestApi): Action()
        data class OnClickLikeApi(val requestApi: RequestApi): Action()
        data object NavigateToOtherScreen: Action()
    }

    sealed class Effect {
        data object NavigateToAddApiScreen: Effect()
        data object NavigateToTestApiScreen: Effect()
    }

    data class State(
        val isLoading: Boolean = false,
        val listApi: List<RequestApi> = emptyList()
    )
}