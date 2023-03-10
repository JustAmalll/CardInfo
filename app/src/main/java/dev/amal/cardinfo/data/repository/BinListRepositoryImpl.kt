package dev.amal.cardinfo.data.repository

import dev.amal.cardinfo.common.utils.Resource
import dev.amal.cardinfo.data.local.HistoryDao
import dev.amal.cardinfo.data.local.entity.HistoryEntity
import dev.amal.cardinfo.data.local.entity.toCardInfo
import dev.amal.cardinfo.data.remote.BinListApi
import dev.amal.cardinfo.domain.model.CardInfo
import dev.amal.cardinfo.domain.repository.BinListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BinListRepositoryImpl @Inject constructor(
    private val api: BinListApi,
    private val dao: HistoryDao
) : BinListRepository {

    override fun getCardInfo(cardBIN: String): Flow<Resource<CardInfo>> = flow {
        try {
            emit(Resource.Loading())
            val cardInfo = api.getCardInfo(cardBIN)

            // Add item only if it is not already in database
            val searchHistory = dao.getHistory()
            val isItemAlreadyInDb = searchHistory.find { it.cardBIN == cardBIN } != null
            if (!isItemAlreadyInDb) {
                dao.insertCardData(
                    HistoryEntity(
                        cardBIN = cardBIN,
                        bank = cardInfo.bank,
                        brand = cardInfo.brand,
                        country = cardInfo.country,
                        number = cardInfo.number,
                        prepaid = cardInfo.prepaid,
                        scheme = cardInfo.scheme,
                        type = cardInfo.type
                    )
                )
            }
            emit(
                Resource.Success(
                    CardInfo(
                        cardBIN = cardBIN,
                        bank = cardInfo.bank,
                        brand = cardInfo.brand,
                        country = cardInfo.country,
                        number = cardInfo.number,
                        prepaid = cardInfo.prepaid,
                        scheme = cardInfo.scheme,
                        type = cardInfo.type
                    )
                )
            )
        } catch (exception: HttpException) {
            when (exception.code()) {
                400 -> emit(Resource.Error("Invalid Card Format"))
                404 -> emit(Resource.Error("No matching cards found"))
                429 -> emit(Resource.Error("Too Many Requests"))
                else -> emit(Resource.Error("An unexpected error occurred"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    override suspend fun getHistory(): List<CardInfo> {
        return dao.getHistory().map { it.toCardInfo() }
    }

    override suspend fun getHistoryItemByCardBIN(cardBIN: String): CardInfo {
        return dao.getHistoryItemByCardBIN(cardBIN).toCardInfo()

    }

    override suspend fun deleteHistoryItemByCardBIN(cardBIN: String) {
        return dao.deleteHistoryItemByCardBIN(cardBIN)
    }

    override suspend fun deleteAllHistory() {
        return dao.deleteAllHistory()
    }
}