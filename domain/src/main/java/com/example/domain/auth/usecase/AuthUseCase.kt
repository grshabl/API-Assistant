package com.example.domain.auth.usecase

import com.example.domain.auth.repository.AuthRepository
import javax.inject.Inject

interface AuthUseCase {
    fun auth(pin: String): Boolean
}

class AuthUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) : AuthUseCase {
    override fun auth(pin: String): Boolean {
        val pinFromStorage = authRepository.getPinAuth()
        return pin == pinFromStorage
    }
}