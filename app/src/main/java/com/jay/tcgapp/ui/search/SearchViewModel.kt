package com.jay.tcgapp.ui.search

import androidx.lifecycle.ViewModel
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.repo.CardsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel(
    private val repo: CardsRepo = CardsRepo.getInstance()
) : ViewModel() {
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    private var searchText: String = ""

    fun setSearchText(text: String) {
        searchText = text
        getCards()
    }

    fun getCards() {
        _cards.value = repo.getFilteredCards(searchText)
    }
}