package com.jay.tcgapp.ui.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.model.Category
import com.jay.tcgapp.data.model.Rarity
import com.jay.tcgapp.data.repo.CardsRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditCardViewModel(
    private val repo: CardsRepo = CardsRepo.getInstance()
) : ViewModel() {
    private val _finish = MutableSharedFlow<Unit>()

    val finish = _finish.asSharedFlow()

    private val _card = MutableStateFlow(Card(title = "", price = 0.0, cardImageUri = "",
        category = Category.NONE, rarity = Rarity.COMMON)
    )

    val card = _card.asStateFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun getCard(id: Int) {
        repo.getCardById(id)?.let {
            _card.value = it
        }
    }

    fun updateCard(title: String, price: Double, imageUri: String, category: Category, rarity: Rarity) {
        try {
            repo.updateCard(card.value.copy(title = title, price = price, cardImageUri = imageUri,
                category = category, rarity = rarity)
            )
            viewModelScope.launch {
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _error.emit("Failed to update card: ${e.message}")
            }
        }
    }

    fun deleteCard(id: Int) = repo.deleteCard(id)
}