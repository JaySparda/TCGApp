package com.jay.tcgapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jay.tcgapp.data.model.Card

@Database(entities = [Card::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

    abstract fun getCardDao(): CardsDao

    companion object {
        const val NAME = "my_database"
    }
}