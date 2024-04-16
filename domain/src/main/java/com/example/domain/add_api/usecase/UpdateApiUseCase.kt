package com.example.domain.add_api.usecase

import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import javax.inject.Inject

interface UpdateApiUseCase {
    suspend fun updateApi(requestApi: RequestApi): Boolean
}

class UpdateApiUseCaseImpl @Inject constructor(
    private val realmRepository: RealmRepository
) : UpdateApiUseCase {

    override suspend fun updateApi(requestApi: RequestApi) =
        realmRepository.updateRequestApi(requestApi)

}