package com.example.apiassistant.model.auth

data class AuthState(
    var pin: String = "",
    var error: String? = null,
    var isCorrectPin : Boolean = false
)