package com.example.apiassistant.ui.screen.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domain.auth.usecase.AuthUseCase
import com.example.apiassistant.model.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {
    private val _state: MutableState<AuthState> = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    fun onAction(action: Action) {
        when (action) {
            is Action.OnClickButtonAuth -> {
                val isCorrectPin = authUseCase.auth(pin = _state.value.pin)
                _state.value = _state.value.copy(isCorrectPin = isCorrectPin)
            }
        }
    }

    fun setPin(newPin: String) {
        _state.value = state.value.copy(pin = newPin)
    }


    sealed class Action {
        object OnClickButtonAuth: Action()
    }

    sealed class AuthEffect {
        object NavigateToMainScreen: AuthEffect()
    }
}