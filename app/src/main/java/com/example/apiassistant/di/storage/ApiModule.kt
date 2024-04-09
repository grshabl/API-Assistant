package com.example.apiassistant.di.storage

import com.example.data.source.api.Network
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ApiModule {

    @Binds
    fun provideNetwork(network: Network): Network

}