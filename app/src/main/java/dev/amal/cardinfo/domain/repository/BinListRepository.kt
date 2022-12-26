package dev.amal.cardinfo.domain.repository

import dev.amal.cardinfo.common.Resource
import dev.amal.cardinfo.domain.model.CardInfo
import kotlinx.coroutines.flow.Flow

interface BinListRepository {
    fun getCardInfo(cardBIN: String): Flow<Resource<CardInfo>>
    suspend fun getHistory(): List<CardInfo>
    suspend fun getHistoryItemByCardBIN(cardBIN: String): CardInfo
    suspend fun deleteHistoryItemByCardBIN(cardBIN: String)
    suspend fun deleteAllHistory()
}