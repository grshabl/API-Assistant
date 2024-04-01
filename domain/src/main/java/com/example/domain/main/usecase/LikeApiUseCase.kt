package com.example.domain.main.usecase

import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import javax.inject.Inject

interface LikeApiUseCase {
    suspend fun likeApi(requestApi: RequestApi, isLike: Boolean): Boolean
}

class LikeApiUseCaseImpl @Inject constructor(
    private val realmRepository: RealmRepository
) : LikeApiUseCase {

    override suspend fun likeApi(requestApi: RequestApi, isLike: Boolean) =
        realmRepository.updateRequestApi(requestApi.copy(isLike = isLike))

}