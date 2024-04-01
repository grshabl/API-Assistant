package com.example.domain.main.usecase

import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import javax.inject.Inject

interface GetApiUseCase {
    suspend fun getAllApi(): List<RequestApi>
}

class GetApiUseCaseImpl @Inject constructor(
    private val realmRepository: RealmRepository
) : GetApiUseCase {

    override suspend fun getAllApi(): List<RequestApi> =
        realmRepository.getRequestsApi()

}