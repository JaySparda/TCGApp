package com.jay.tcgapp.data.repo

import com.jay.tcgapp.data.db.CardsDao
import com.jay.tcgapp.data.model.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardsRepo(
    private val dao: CardsDao
) {

    suspend fun addCard(card: Card) {
        dao.addCard(card)
    }

    suspend fun getCardById(id: Int): Card? {
        return dao.getCardById(id)
    }

    suspend fun deleteCard(id: Int) {
        dao.delete(id)
    }

    suspend fun updateCard(card: Card) {
        dao.update(card)
    }

    suspend fun collectedCard(id: Int, collect: Boolean) {
        val card = dao.getCardById(id) ?: return
        dao.update(card.copy(collected = collect))
    }

    private fun filterSearch(list: List<Card>, searchText: String): List<Card> {
        if(searchText.isBlank()) return list
        val text = searchText.lowercase()
        return list.filter {
            it.title.lowercase().contains(text)
        }
    }

    fun getFilteredCard(searchText: String): Flow<List<Card>> {
        return dao.getAllCards().map { cards ->
            var result = cards.filter { !it.collected }
            result= filterSearch(result, searchText)
            result
        }
    }

    fun getFilteredCollectedCard(searchText: String): Flow<List<Card>> {
        return dao.getAllCards().map { cards ->
            var result = cards.filter { it.collected }
            result = filterSearch(result, searchText)
            result
        }
    }
}