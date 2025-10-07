package com.jay.tcgapp.ui.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jay.tcgapp.MyApp
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.repo.CardsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddCardViewModel(
    private val repo: CardsRepo
) : ViewModel() {
    private val _finish = MutableSharedFlow<Unit>()

    val finish = _finish.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    fun addCard(card: Card) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.addCard(card)
                _finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit("Failed to add card: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).repo
                AddCardViewModel(myRepository)
            }
        }
    }
}