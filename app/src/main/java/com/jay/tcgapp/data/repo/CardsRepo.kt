package com.jay.tcgapp.data.repo

import com.jay.tcgapp.data.model.Card

class CardsRepo private constructor() {
    private val cards: MutableMap<Int, Card> = mutableMapOf()
    private var counter = 0

    fun addCard(card: Card) {
        counter++
        val newCard = card.copy(id = counter)
        cards[counter] = newCard
    }

    fun getCardById(id: Int): Card? {
        return cards[id]
    }

    fun getAllCards(): List<Card> = cards.values.filter { !it.collected }
    fun getAllCollectedCards(): List<Card> = cards.values.filter { it.collected }

    fun deleteCard(id: Int) {
        cards.remove(id)
    }

    fun updateCard(card: Card) {
        cards[card.id] = card
    }

    fun collectedCard(id: Int, collect: Boolean = true) {
        val card = cards[id] ?: return
        cards[id] = card.copy(collected = collect)
    }

    private fun filterSearch(list: List<Card>, searchText: String): List<Card> {
        if(searchText.isBlank()) return list
        val text = searchText.lowercase()
        return list.filter {
            it.title.lowercase().contains(text)
        }
    }

    fun getFilteredCards(searchText: String): List<Card> {
        var result = getAllCards()
        result = filterSearch(result, searchText)
        return result
    }

    fun getFilteredCollectedCard(searchText: String): List<Card> {
        var result = getAllCollectedCards()
        result = filterSearch(result, searchText)
        return result
    }

    companion object {
        private var instance: CardsRepo? = null

        fun getInstance(): CardsRepo {
            if(instance == null){
                instance = CardsRepo()
            }
            return instance!!
        }
    }
}