package com.jay.tcgapp.ui.detail

import androidx.lifecycle.ViewModel
import com.jay.tcgapp.data.repo.CardsRepo

class DetailViewModel(
    private val repo: CardsRepo = CardsRepo.getInstance()
) : ViewModel() {

    fun getCardById(id: Int) = repo.getCardById(id)
}