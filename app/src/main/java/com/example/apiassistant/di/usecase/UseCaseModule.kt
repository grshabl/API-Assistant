package com.example.apiassistant.di.usecase

import com.example.domain.add_api.AddApiUseCase
import com.example.domain.add_api.AddApiUseCaseImpl
import com.example.domain.api.usecase.ParserUseCase
import com.example.domain.api.usecase.ParserUseCaseImpl
import com.example.domain.main.usecase.DeleteApiUseCase
import com.example.domain.main.usecase.DeleteApiUseCaseImpl
import com.example.domain.main.usecase.GetApiUseCase
import com.example.domain.main.usecase.GetApiUseCaseImpl
import com.example.domain.main.usecase.LikeApiUseCase
import com.example.domain.main.usecase.LikeApiUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun provideGetApiUseCase(getApiUseCaseImpl: GetApiUseCaseImpl) : GetApiUseCase

    @Binds
    fun provideAddApiUseCase(addApiUseCaseImpl: AddApiUseCaseImpl): AddApiUseCase

    @Binds
    fun provideDeleteApiUseCase(deleteApiUseCaseImpl: DeleteApiUseCaseImpl): DeleteApiUseCase

    @Binds
    fun provideLikeApiUseCase(likeApiUseCaseImpl: LikeApiUseCaseImpl): LikeApiUseCase

    @Binds
    fun provideParserUseCase(parserUseCaseImpl: ParserUseCaseImpl): ParserUseCase

}