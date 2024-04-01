package com.example.domain.add_api

import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import javax.inject.Inject

interface AddApiUseCase {
    suspend fun addApi(requestApi: RequestApi): Boolean
    suspend fun addApi(listApi: List<RequestApi>): Boolean

}

class AddApiUseCaseImpl @Inject constructor(
    private val realmRepository: RealmRepository
) : AddApiUseCase {

    override suspend fun addApi(requestApi: RequestApi) =
        realmRepository.createRequestApi(requestApi)

    override suspend fun addApi(listApi: List<RequestApi>) =
        realmRepository.createRequestApi(listApi)

}