package com.jay.tcgapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.jay.tcgapp.R
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.repo.CardsRepo
import com.jay.tcgapp.databinding.ItemLayoutCardBinding

class CardAdapter(
    private var cards: List<Card> = emptyList(),
    private val onCardClick: (Card) -> Unit
): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemLayoutCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    fun setCards(cards: List<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    inner class CardViewHolder(
        private val binding: ItemLayoutCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(card: Card) {
            binding.tvTitle.text = card.title
            binding.tvPrice.text = card.price.toString()
            binding.tvCategory.text = card.category.toString()
            binding.tvRarity.text = card.rarity.toString()
            binding.root.setOnClickListener { onCardClick(card) }
            if(card.cardImageUri.isNotEmpty()) {
                binding.ivCard.setImageURI(card.cardImageUri.toUri())
            } else {
                binding.ivCard.setImageResource(R.drawable.giratinaex)
            }
        }
    }
}