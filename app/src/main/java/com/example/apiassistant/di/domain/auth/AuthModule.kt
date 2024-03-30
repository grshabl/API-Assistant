//package com.example.apiassistant.di.domain.auth
//
//import com.example.domain.auth.repository.AuthRepository
//import com.example.domain.auth.usecase.AuthUseCase
//import com.example.domain.auth.usecase.AuthUseCaseImpl
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//
//@Module
//@InstallIn(SingletonComponent::class)
//interface AuthModule {
//
//    @Binds
//    fun provideAuthUseCase(authUseCaseImpl: AuthUseCaseImpl) : AuthUseCase
//
//    @Binds
//    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
//
//}