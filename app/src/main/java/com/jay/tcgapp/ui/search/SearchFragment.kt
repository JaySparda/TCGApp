package com.jay.tcgapp.ui.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jay.tcgapp.R
import com.jay.tcgapp.databinding.FragmentSearchBinding
import com.jay.tcgapp.ui.adapter.CardAdapter
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        lifecycleScope.launch {
            viewModel.cards.collect {
                adapter.setCards(it)
                binding.llEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAdd.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragment2ToAddCardFragment()
            findNavController().navigate(action)
        }

        setFragmentResultListener("manage_card") { _, _ ->
            viewModel.getCards()
        }
    }

    fun setupAdapter() {
        adapter = CardAdapter(emptyList()) {
            val action = SearchFragmentDirections.actionSearchFragment2ToDetailFragment(it.id)
            findNavController().navigate(action)
        }
        binding.rvCards.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCards.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCards()
    }
}