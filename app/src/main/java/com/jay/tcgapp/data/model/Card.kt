package com.jay.tcgapp.data.model

import android.net.Uri

data class Card (
    val id: Int = 0,
    val title: String,
    val price: Double,
    val cardImageUri: String = "",
    val category: Category = Category.NONE,
    val rarity: Rarity = Rarity.COMMON
)

enum class Category{
    NONE, BLACKBOLT, WHITEFLARE
}

enum class Rarity{
    COMMON, UNCOMMON, RARE
}