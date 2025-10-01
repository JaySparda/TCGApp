package com.jay.tcgapp.ui.manage

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jay.tcgapp.data.model.Card
import com.jay.tcgapp.data.model.Category
import com.jay.tcgapp.data.model.Rarity
import com.jay.tcgapp.databinding.FragmentManageCardBinding
import kotlinx.coroutines.launch

class AddCardFragment : Fragment() {
    private val viewModel: AddCardViewModel by viewModels()
    private lateinit var binding: FragmentManageCardBinding

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

        lifecycleScope.launch {
            viewModel.finish.collect {
                setFragmentResult("manage_card", Bundle())
                findNavController().popBackStack()
            }
        }

//        binding.run {
//            mbAdd.setOnClickListener {
//                val title = etTitle.text.toString()
//                val imageUri = mbImg.text.toString()
//                val price = etPrice.text.toString().toDouble()
//                val category = categorySpinner.selectedItem
//                val rarity = raritySpinner.selectedItem
//                val card = Card(
//                    title = title,
//                    price = price,
//                    cardImageUri = imageUri,
//                    category = category as Category,
//                    rarity = rarity as Rarity
//                )
//                viewModel.addCard(card)
//            }
//            mbImg.setOnClickListener {
//                openGallery()
//            }
//        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
    }
}