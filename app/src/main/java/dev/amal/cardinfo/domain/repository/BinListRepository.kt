package dev.amal.cardinfo.domain.repository

import dev.amal.cardinfo.data.remote.dto.CardInfoDto

interface BinListRepository {
    suspend fun getCardInfo(cardBIN: String): CardInfoDto
}