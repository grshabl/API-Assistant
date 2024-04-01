package com.example.domain.main.usecase

import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import javax.inject.Inject

interface DeleteApiUseCase {
    suspend fun deleteApi(requestApi: RequestApi): Boolean
}

class DeleteApiUseCaseImpl @Inject constructor(
    private val realmRepository: RealmRepository
) : DeleteApiUseCase {

    override suspend fun deleteApi(requestApi: RequestApi) =
        realmRepository.deleteRequestApi(requestApi)

}