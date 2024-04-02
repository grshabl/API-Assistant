package com.example.apiassistant.di.repository

import com.example.data.repository.api.TestApiRepositoryImpl
import com.example.domain.test_api.repository.TestApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideTestApiRepository(testApiRepositoryImpl: TestApiRepositoryImpl): TestApiRepository

}