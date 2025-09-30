package com.jay.tcgapp.data.repo

import com.jay.tcgapp.data.model.Card

class CardsRepo private constructor() {
    private val cards = mutableMapOf<Int, Card>()
    private var counter = 0

    init {
        generateRandomCard(5)
    }

    fun addCard(card: Card) {
        counter++
        val newCard = card.copy(id = counter)
        cards[counter] = newCard
    }

    fun getCardById(id: Int): Card? {
        return cards[id]
    }

    fun getAllCards(): List<Card> = cards.values.toList()
    fun getAllCollectedCards(): List<Card> = cards.values.filter { it.collected }

    fun deleteCard(id: Int) {
        cards.remove(id)
    }

    fun updateCard(id: Int, card: Card) {
        cards[id] = card
    }

    fun generateRandomCard(n: Int) {
        repeat(n) { i ->
            cards[counter++] = Card(
                title = "Pokemon $i",
                price = (1..100).random().toDouble(),
            )
        }
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