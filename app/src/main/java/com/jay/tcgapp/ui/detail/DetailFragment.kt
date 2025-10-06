package com.jay.tcgapp.ui.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jay.tcgapp.R
import com.jay.tcgapp.databinding.FragmentDetailBinding
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResult

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val card = viewModel.getCardById(args.cardId)
        if(card == null) {
            findNavController().popBackStack()
            return
        }

        binding.run {
            if(card.cardImageUri.isNotEmpty()) {
                ivCard.setImageURI(card.cardImageUri.toUri())
            } else {
                ivCard.setImageResource(R.drawable.giratinaex)
            }
            tvTitle.text = card.title
            tvPrice.text = card.price.toString()
            tvCategory.text = card.category.name
            tvRarity.text = card.rarity.name
            mbCollect.text =
                if(card.collected) getString(R.string.remove_from_collection)
                else getString(R.string.add_to_collection)

            mbCollect.setOnClickListener {
                val isCollected = viewModel.getCardById(card.id)?.collected == true
                if(isCollected) {
                    viewModel.markCollected(card.id, collected = false)
                } else {
                    viewModel.markCollected(card.id)
                }
                setFragmentResult("manage_card", Bundle().apply { putBoolean("refresh", true) })
                findNavController().popBackStack()
            }
            mbEdit.setOnClickListener {
                val action = DetailFragmentDirections.actionDetailFragmentToEditFragment(card.id)
                findNavController().navigate(action)
            }
        }
    }
}