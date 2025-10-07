package com.jay.tcgapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jay.tcgapp.data.model.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {

    @Query("SELECT * FROM card")
    fun getAllCards(): Flow<List<Card>>

    @Query("SELECT * FROM card WHERE id = :id")
    suspend fun getCardById(id: Int): Card?

    @Insert
    suspend fun addCard(card: Card)

    @Update
    suspend fun update(card: Card)

    @Query("DELETE FROM card WHERE id = :id")
    suspend fun delete(id: Int)
}