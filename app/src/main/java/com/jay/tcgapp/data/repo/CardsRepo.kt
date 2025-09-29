package com.jay.tcgapp.data.repo

import com.jay.tcgapp.data.model.Card

class CardsRepo private constructor() {
    private val cards = mutableMapOf<Int, Card>()
    private var counter = 0

    fun addCard(card: Card) {
        counter++
        val newCard = card.copy(id = counter)
        cards[counter] = newCard
    }

    fun getCardById(id: Int): Card? {
        return cards[id]
    }

    fun getAllCards() = cards.values.toList()

    fun deleteCard(id: Int) {
        cards.remove(id)
    }

    fun updateCard(id: Int, card: Card) {
        cards[id] = card
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