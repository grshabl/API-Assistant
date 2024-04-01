package com.example.apiassistant.ui.screen.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.api.model.RequestApi
import com.example.domain.main.usecase.DeleteApiUseCase
import com.example.domain.main.usecase.GetApiUseCase
import com.example.domain.main.usecase.LikeApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getApiUseCase: GetApiUseCase,
    private val likeApiUseCase: LikeApiUseCase,
    private val deleteApiUseCase: DeleteApiUseCase
) : ViewModel() {

    private val _state: MutableState<State> = mutableStateOf(State())
    val state: androidx.compose.runtime.State<State> = _state

    private val _effect: MutableState<Effect?> = mutableStateOf(null)
    val effect: androidx.compose.runtime.State<Effect?> = _effect

    init {
        viewModelScope.launch {
            _state.value = State(isLoading = true)
            val listApi = getApiUseCase.getAllApi()
            _state.value = state.value.copy(
                isLoading = false,
                listApi = listApi
            )
        }
    }

    fun updateState() {
        viewModelScope.launch {
            val listApi = getApiUseCase.getAllApi()
            _state.value = state.value.copy(
                listApi = listApi
            )
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
        _effect.value = Effect.NavigateToTestApiScreen(requestApi)
    }
    private fun onClickLikeApi(requestApi: RequestApi) {
        viewModelScope.launch {
            val isUpdate = likeApiUseCase.likeApi(requestApi, !requestApi.isLike)
            if (isUpdate) {
                _state.value = state.value.copy(listApi = getApiUseCase.getAllApi())
            }
        }
    }

    private fun onClickDeleteApi(requestApi: RequestApi) {
        viewModelScope.launch {
            val isUpdate = deleteApiUseCase.deleteApi(requestApi)
            if (isUpdate) {
                _state.value = state.value.copy(listApi = getApiUseCase.getAllApi())
            }
        }
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
        data class NavigateToTestApiScreen(val requestApi: RequestApi): Effect()
    }

    data class State(
        val isLoading: Boolean = false,
        val listApi: List<RequestApi> = emptyList()
    )
}