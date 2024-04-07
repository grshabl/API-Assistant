package com.example.domain.main.usecase

import com.example.domain.api.model.RequestApi
import com.example.domain.common.repository.RealmRepository
import org.apache.commons.text.similarity.JaccardSimilarity
import javax.inject.Inject

interface DetectVoiceCommandUseCase {
    suspend fun detectVoiceCommand(detectedText: String) : RequestApi?
}

class DetectVoiceCommandUseCaseImpl @Inject constructor(
    private val realmRepository: RealmRepository
): DetectVoiceCommandUseCase {

    override suspend fun detectVoiceCommand(detectedText: String) : RequestApi? {
        val listRequestApi = realmRepository.getRequestsApi()

        // find 100% similarity
        listRequestApi.firstOrNull { it.voiceString == detectedText }?.let {
            return it
        }

        // else find 80% similarity
        for (requestApi in listRequestApi) {
            requestApi.voiceString?.let {
                if (getPercentSimilarity(it, detectedText) >= MIN_PERCENT_SIMILARITY) {
                    return requestApi
                }
            }
        }

        return null
    }

    private fun getPercentSimilarity(string1: String, string2: String): Double {
        val jaccardSimilarity = JaccardSimilarity()
        val similarityScore = jaccardSimilarity.apply(string1, string2)
        val percentage = (similarityScore * 100).toInt()

        return percentage.toDouble()
    }

    companion object {
        private const val MIN_PERCENT_SIMILARITY = 80
    }

}