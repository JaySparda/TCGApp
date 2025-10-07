package com.jay.tcgapp.ui.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jay.tcgapp.MyApp
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.model.Category
import com.jay.tcgapp.data.model.Rarity
import com.jay.tcgapp.data.repo.CardsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditCardViewModel(
    private val repo: CardsRepo
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
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCardById(id)?.let {
                _card.value = it
            }
        }
    }

    fun updateCard(title: String, price: Double, imageUri: String, category: Category, rarity: Rarity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.updateCard(card.value.copy(title = title, price = price, cardImageUri = imageUri,
                    category = category, rarity = rarity)
                )
                    _finish.emit(Unit)
            } catch (e: Exception) {
                    _error.emit("Failed to update card: ${e.message}")
            }
        }
    }

    fun deleteCard(id: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteCard(id)
        }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).repo
                EditCardViewModel(myRepository)
            }
        }
    }
}