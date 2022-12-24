package dev.amal.cardinfo.domain.repository

import dev.amal.cardinfo.common.Resource
import dev.amal.cardinfo.domain.model.CardInfo
import kotlinx.coroutines.flow.Flow

interface BinListRepository {
    fun getCardInfo(cardBIN: String): Flow<Resource<CardInfo>>
    suspend fun getSearchHistory(): List<CardInfo>
    suspend fun getItemFromSearchHistory(cardBIN: String): CardInfo
    suspend fun deleteSearchHistoryItem(cardBIN: String)
    suspend fun deleteAllSearchHistory()
}