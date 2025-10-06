package com.jay.tcgapp.ui.manage

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jay.tcgapp.R
import com.jay.tcgapp.data.model.Category
import com.jay.tcgapp.data.model.Rarity
import com.jay.tcgapp.databinding.FragmentManageCardBinding
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class EditCardFragment : Fragment() {

    private val viewModel: EditCardViewModel by viewModels()

    private lateinit var binding: FragmentManageCardBinding

    private val args: EditCardFragmentArgs by navArgs()

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

        viewModel.getCard(args.cardId)

        setupSpinners()

        lifecycleScope.launch {
            viewModel.finish.collect {
                setFragmentResult("manage_card", Bundle())
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

        lifecycleScope.launch {
            viewModel.card.collect {
                binding.run {
                    it.cardImageUri.takeIf { uri -> uri.isNotEmpty() }?.let { uriString ->
                        ivCard.setImageURI(uriString.toUri())
                    } ?: run {
                        ivCard.setImageResource(R.drawable.giratinaex)
                    }
                    etTitle.setText(it.title)
                    etPrice.setText(it.price.toString())
                    categorySpinner.setSelection(it.category.ordinal)
                    raritySpinner.setSelection(it.rarity.ordinal)
                }
            }
        }

        binding.run {
            llAdd.visibility = View.GONE

            mbImg.setOnClickListener {
                openGallery()
            }
            ivCard.setOnClickListener {
                openGallery()
            }

            mbUpdate.setOnClickListener {
                val title = etTitle.text.toString()
                val imageUri = mbImg.text.toString()
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

                viewModel.updateCard(
                    title = title,
                    price = price,
                    imageUri = imageUri,
                    category = category,
                    rarity = rarity
                )
            }

            mbDelete.setOnClickListener {
                viewModel.deleteCard(args.cardId)
                setFragmentResult("manage_card", Bundle())
                findNavController().popBackStack()
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