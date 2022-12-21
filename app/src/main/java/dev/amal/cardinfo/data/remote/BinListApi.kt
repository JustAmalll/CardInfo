package dev.amal.cardinfo.data.remote

import dev.amal.cardinfo.data.remote.dto.CardInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BinListApi {

    @GET("{cardBIN}")
    suspend fun getCardInfo(
        @Path("cardBIN") cardBIN: String
    ): CardInfoDto

}