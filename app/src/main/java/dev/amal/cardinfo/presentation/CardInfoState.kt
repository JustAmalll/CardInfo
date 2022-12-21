package dev.amal.cardinfo.presentation

import dev.amal.cardinfo.domain.model.CardInfo

data class CardInfoState(
    val isLoading: Boolean = false,
    val cardInfo: CardInfo? = null,
    val error: String = ""
)