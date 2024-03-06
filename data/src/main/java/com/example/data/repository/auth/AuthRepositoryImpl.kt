package com.example.data.repository.auth

import com.example.data.source.RAMStorage
import com.example.domain.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(): AuthRepository {
    override fun getPinAuth() : String =
        RAMStorage.getPinAuth()
}