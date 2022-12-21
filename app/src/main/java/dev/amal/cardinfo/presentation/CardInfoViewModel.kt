package dev.amal.cardinfo.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.cardinfo.common.Resource
import dev.amal.cardinfo.domain.use_cases.get_card_info.GetCardInfoUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CardInfoViewModel @Inject constructor(
    private val getCardInfoUseCase: GetCardInfoUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CardInfoState())
    val state: State<CardInfoState> = _state

    var cardNumber by mutableStateOf("45717360")

    fun getCardInfo() {
        getCardInfoUseCase(cardNumber).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = CardInfoState(cardInfo = result.data)
                }
                is Resource.Error -> {
                    _state.value = CardInfoState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = CardInfoState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}