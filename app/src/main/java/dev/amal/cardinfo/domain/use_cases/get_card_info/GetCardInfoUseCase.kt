package dev.amal.cardinfo.domain.use_cases.get_card_info

import dev.amal.cardinfo.common.Resource
import dev.amal.cardinfo.data.remote.dto.toCardInfo
import dev.amal.cardinfo.domain.model.CardInfo
import dev.amal.cardinfo.domain.repository.BinListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCardInfoUseCase @Inject constructor(
    private val repository: BinListRepository
) {
    operator fun invoke(cardBIN: String): Flow<Resource<CardInfo>> = flow {
        try {
            emit(Resource.Loading())
            val cardInfo = repository.getCardInfo(cardBIN).toCardInfo()
            emit(Resource.Success(cardInfo))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}