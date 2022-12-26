package dev.amal.cardinfo.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.cardinfo.common.Resource
import dev.amal.cardinfo.common.removeWhitespaces
import dev.amal.cardinfo.domain.repository.BinListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardInfoViewModel @Inject constructor(
    private val repository: BinListRepository
) : ViewModel() {

    private val _state = mutableStateOf(CardInfoState())
    val state: State<CardInfoState> = _state

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    // Card number value for text field
    var cardNumber by mutableStateOf("")

    init {
        // Get search history when screen is shown
        getHistory()
    }

    fun getCardInfo() {
        if (cardNumber.isEmpty()) {
            viewModelScope.launch {
                _snackBarEvent.emit("BIN/IIN of the card cannot be empty")
            }
            return
        }

        repository.getCardInfo(cardNumber.removeWhitespaces())
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            cardInfo = result.data, isLoading = false
                        )
                        // Updating ui after searching card information
                        getHistory()
                    }
                    is Resource.Error -> {
                        _snackBarEvent.emit(result.message ?: "An unexpected error occurred")
                        _state.value = state.value.copy(isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun getHistory() {
        viewModelScope.launch {
            val result = repository.getHistory()
            _state.value = state.value.copy(history = result)
        }
    }

    fun deleteHistoryItemByCardBIN(cardBIN: String) {
        viewModelScope.launch {
            repository.deleteHistoryItemByCardBIN(cardBIN)
            // Updating ui after deleting item
            getHistory()
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
            // Updating ui after deleting all items
            getHistory()
        }
    }

    fun getHistoryItemByCardBIN(cardBIN: String) {
        // if history item is not item just searched - load from database
        if (cardBIN != state.value.cardInfo?.cardBIN) {
            viewModelScope.launch {
                val result = repository.getHistoryItemByCardBIN(cardBIN)
                _state.value = state.value.copy(cardInfo = result)
            }
        }
    }
}