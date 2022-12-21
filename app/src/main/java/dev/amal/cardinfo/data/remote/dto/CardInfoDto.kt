package dev.amal.cardinfo.data.remote.dto

import dev.amal.cardinfo.domain.model.CardInfo

data class CardInfoDto(
    val bank: Bank,
    val brand: String,
    val country: Country,
    val number: Number,
    val prepaid: Boolean,
    val scheme: String,
    val type: String
)

fun CardInfoDto.toCardInfo(): CardInfo = CardInfo(
    bank = bank,
    brand = brand,
    country = country,
    number = number,
    prepaid = prepaid,
    scheme = scheme,
    type = type
)