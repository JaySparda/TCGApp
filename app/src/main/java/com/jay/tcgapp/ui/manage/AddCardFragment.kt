package com.jay.tcgapp.ui.manage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jay.tcgapp.R
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.model.Category
import com.jay.tcgapp.data.model.Rarity
import com.jay.tcgapp.databinding.FragmentManageCardBinding
import kotlinx.coroutines.launch

class AddCardFragment : Fragment() {
    private val viewModel: AddCardViewModel by viewModels{
        AddCardViewModel.Factory
    }
    private lateinit var binding: FragmentManageCardBinding
    private var selectedImageUri: String = ""

    private val openGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri.toString()
            binding.ivCard.setImageURI(uri)
            binding.mbImg.text = getString(R.string.change_image)
        } else {
            showError("No Selected Image")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageCardBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()

        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack()
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMsg ->
                if (errorMsg.isNotEmpty()) {
                    showError(errorMsg)
                }
            }
        }

        binding.run {
            llBtn.visibility = View.GONE

            mbImg.setOnClickListener {
                openGallery()
            }

            ivCard.setOnClickListener {
                openGallery()
            }

            mbAdd.setOnClickListener {
                val title = binding.etTitle.text.toString()
                val imageUri = selectedImageUri
                val priceText = binding.etPrice.text.toString()

                when {
                    title.isEmpty() -> {
                        showError("Title cannot be empty")
                    }
                    imageUri.isEmpty() -> {
                        showError("Please select an image")
                    }
                    priceText.isEmpty() -> {
                        showError("Price cannot be empty")
                    }
                    !isValidPrice(priceText) -> {
                        showError("Invalid price")
                    }
                }
                val price = priceText.toDouble()
                val categoryString= binding.categorySpinner.selectedItem.toString()
                val rarityString = binding.raritySpinner.selectedItem.toString()

                val category = getCategoryFromString(categoryString)
                val rarity = getRarityFromString(rarityString)

                val card = Card(
                    title = title,
                    price = price,
                    cardImageUri = imageUri,
                    category = category,
                    rarity = rarity
                )
                viewModel.addCard(card)
            }
        }
    }
    private fun setupSpinners() {
        //Categories
        val categories = enumValues<Category>().map { it.cat }
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = categoryAdapter
        //Rarities
        val rarities = enumValues<Rarity>().map { it.rarity }
        val rarityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            rarities
        )
        rarityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.raritySpinner.adapter = rarityAdapter
    }

    private fun getCategoryFromString(displayName: String): Category {
        return enumValues<Category>().find { it.cat == displayName } ?: Category.NONE
    }

    private fun getRarityFromString(displayName: String): Rarity {
        return enumValues<Rarity>().find { it.rarity == displayName } ?: Rarity.COMMON
    }

    private fun isValidPrice(priceText: String): Boolean {
        return try {
            val price = priceText.toDouble()
            price >= 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun openGallery() {
        openGallery.launch("image/*")
    }

    fun showError(msg: String) {
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(
            ContextCompat.getColor
                (requireContext(), R.color.pokemon_red)
        )
        snackbar.show()
    }
}