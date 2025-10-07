package com.jay.tcgapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Card (
    @PrimaryKey(true)
    val id: Int = 0,
    val title: String,
    val price: Double,
    val cardImageUri: String = "",
    val category: Category = Category.NONE,
    val rarity: Rarity = Rarity.COMMON,
    var collected: Boolean = false
)

enum class Category(val cat: String)
{
    NONE("None"),
    BLACKBOLT("Black Bolt"),
    WHITEFLARE("White Flare");

    override fun toString(): String = cat
}

enum class Rarity(val rarity: String)
{
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare");

    override fun toString(): String = rarity
}