package dev.amal.cardinfo.domain.model

import dev.amal.cardinfo.data.remote.dto.Bank
import dev.amal.cardinfo.data.remote.dto.Number
import dev.amal.cardinfo.data.remote.dto.Country

data class CardInfo(
    val bank: Bank,
    val brand: String,
    val country: Country,
    val number: Number,
    val prepaid: Boolean,
    val scheme: String,
    val type: String
)