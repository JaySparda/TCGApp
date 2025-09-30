package com.jay.tcgapp.ui.home

import androidx.lifecycle.ViewModel
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.repo.CardsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    private val repo: CardsRepo = CardsRepo.getInstance()
) : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    init {
        getCards()
    }

    fun getCards() {
        _cards.value = repo.getAllCards()
    }
}