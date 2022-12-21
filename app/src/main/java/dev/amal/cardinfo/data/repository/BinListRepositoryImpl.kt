package dev.amal.cardinfo.data.repository

import dev.amal.cardinfo.data.remote.BinListApi
import dev.amal.cardinfo.data.remote.dto.CardInfoDto
import dev.amal.cardinfo.domain.repository.BinListRepository
import javax.inject.Inject

class BinListRepositoryImpl @Inject constructor(
    private val api: BinListApi
) : BinListRepository {

    override suspend fun getCardInfo(cardBIN: String): CardInfoDto {
        return api.getCardInfo(cardBIN)
    }
}