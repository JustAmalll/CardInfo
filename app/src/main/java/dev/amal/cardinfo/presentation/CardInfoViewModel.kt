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
        getSearchHistory()
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
                        getSearchHistory()
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

    private fun getSearchHistory() {
        viewModelScope.launch {
            val result = repository.getSearchHistory()
            _state.value = state.value.copy(searchHistory = result)
        }
    }

    fun deleteSearchHistoryItem(cardBin: String) {
        viewModelScope.launch {
            repository.deleteSearchHistoryItem(cardBin)
            // Updating ui after deleting item
            getSearchHistory()
        }
    }

    fun deleteAllSearchHistory() {
        viewModelScope.launch {
            repository.deleteAllSearchHistory()
            // Updating ui after deleting all items
            getSearchHistory()
        }
    }

    fun getItemFromSearchHistory(cardBin: String) {
        // if history item is not item just searched - load from database
        if (cardBin != state.value.cardInfo?.cardBIN) {
            viewModelScope.launch {
                val result = repository.getItemFromSearchHistory(cardBin)
                _state.value = state.value.copy(cardInfo = result)
            }
        }
    }
}