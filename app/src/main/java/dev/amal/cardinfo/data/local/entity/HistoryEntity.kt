package dev.amal.cardinfo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.amal.cardinfo.data.remote.dto.Bank
import dev.amal.cardinfo.data.remote.dto.Number
import dev.amal.cardinfo.data.remote.dto.Country
import dev.amal.cardinfo.domain.model.CardInfo

@Entity
data class HistoryEntity(
    @PrimaryKey val id: Int? = null,
    val cardBIN: String,
    val bank: Bank?,
    val brand: String?,
    val country: Country?,
    val number: Number?,
    val prepaid: Boolean?,
    val scheme: String?,
    val type: String?
)

fun HistoryEntity.toCardInfo(): CardInfo = CardInfo(
    cardBIN = cardBIN,
    bank = bank,
    brand = brand,
    country = country,
    number = number,
    prepaid = prepaid,
    scheme = scheme,
    type = type
)