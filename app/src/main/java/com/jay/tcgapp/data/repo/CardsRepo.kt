package com.jay.tcgapp.data.repo

import com.jay.tcgapp.data.model.Card

class CardsRepo private constructor() {
    private val cards: MutableMap<Int, Card> = mutableMapOf()
    private var counter = 0

    init {
        generateRandomCollectedCard(3)
    }

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


    fun generateRandomCollectedCard(n: Int) {
        repeat(n) { i ->
            cards[counter++] = Card(
                title = "Pokemon $i",
                price = (1..100).random().toDouble(),
                collected = true
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