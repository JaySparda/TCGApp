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

    fun addCard(card: Card) {
        repo.addCard(card)
        viewModelScope.launch {
            _finish.emit(Unit)
        }
    }
}