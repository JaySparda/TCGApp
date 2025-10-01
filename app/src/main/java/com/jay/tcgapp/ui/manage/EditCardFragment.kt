package com.jay.tcgapp.ui.manage

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jay.tcgapp.R
import com.jay.tcgapp.data.model.Category
import com.jay.tcgapp.data.model.Rarity
import com.jay.tcgapp.databinding.FragmentManageCardBinding
import kotlinx.coroutines.launch

class EditCardFragment : Fragment() {

    private val viewModel: EditCardViewModel by viewModels()

    private lateinit var binding: FragmentManageCardBinding

    private val args: EditCardFragmentArgs by navArgs()

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

        lifecycleScope.launch {
            viewModel.finish.collect {
                setFragmentResult("manage_card", Bundle())
                findNavController().popBackStack()
            }
        }

        lifecycleScope.launch {
            viewModel.card.collect {
                binding.run {
//                    ivCard.setImageURI(it.cardImageUri as Uri)
                    etTitle.setText(it.title)
                    etPrice.setText(it.price.toString())
                    categorySpinner.setSelection(it.category.ordinal)
                    raritySpinner.setSelection(it.rarity.ordinal)
                }
            }
        }

        binding.run {
            mbAdd.text = getString(R.string.update)
            mbAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val imageUri = mbImg.text.toString()
                val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
                val category = categorySpinner.selectedItem
                val rarity = raritySpinner.selectedItem
                viewModel.updateCard(title = title, price = price, imageUri = imageUri, category = category as Category, rarity = rarity as Rarity)
            }
        }
    }
}