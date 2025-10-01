package com.jay.tcgapp.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jay.tcgapp.R
import com.jay.tcgapp.databinding.FragmentHomeBinding
import com.jay.tcgapp.ui.adapter.CardAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        binding.btnSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment2()
            findNavController().navigate(action)
        }
    }

    fun setupAdapter() {
        adapter = CardAdapter(emptyList()) {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.id)
            findNavController().navigate(action)
        }

        binding.rvCards.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCards.adapter = adapter
    }

}