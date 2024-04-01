package com.example.apiassistant.di.storage

import com.example.data.repository.realm.RealmRepositoryImpl
import com.example.domain.common.repository.RealmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RealmModule {

    @Binds
    fun provideRealmRepository(realmRepositoryImpl: RealmRepositoryImpl): RealmRepository

}