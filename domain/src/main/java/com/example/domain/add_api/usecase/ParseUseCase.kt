package com.example.domain.add_api.usecase

import com.example.domain.add_api.repository.ParseRepository
import com.example.domain.api.model.RequestApi
import javax.inject.Inject

interface ParseUseCase {
    suspend fun parse(url: String): List<RequestApi>?
}

class ParseUseCaseImpl @Inject constructor(
    private val parseRepository: ParseRepository
) : ParseUseCase {

    override suspend fun parse(url: String): List<RequestApi>? = parseRepository.parse(url)

}