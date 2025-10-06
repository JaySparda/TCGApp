package com.jay.tcgapp.ui.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.repo.CardsRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddCardViewModel(
    private val repo: CardsRepo = CardsRepo.getInstance()
) : ViewModel() {
    private val _finish = MutableSharedFlow<Unit>()

    val finish = _finish.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    fun addCard(card: Card) {
        try {
            repo.addCard(card)
            viewModelScope.launch {
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _error.emit("Failed to add card: ${e.message}")
            }
        }
    }
}