package com.jay.tcgapp.ui.detail

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: CardsRepo
) : ViewModel() {

    private val _card = MutableStateFlow<Card?>(null)
    val card: StateFlow<Card?> = _card
    fun getCardById(id: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            _card.value = repo.getCardById(id)
        }

    fun markCollected(id: Int, collected: Boolean = true) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.collectedCard(id, collected)
        }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).repo
                DetailViewModel(myRepository)
            }
        }
    }
}