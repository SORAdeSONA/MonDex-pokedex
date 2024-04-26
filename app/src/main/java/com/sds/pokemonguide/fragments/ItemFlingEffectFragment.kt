package com.sds.pokemonguide.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.adapter.ItemFlingEffectAdapter
import com.sds.pokemonguide.databinding.ItemFlingEffectFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ItemFlingEffectFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: ItemFlingEffectFragmentArgs by navArgs()

    private var itemFlingEffectAdapter: ItemFlingEffectAdapter? = null

    private var _binding: ItemFlingEffectFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusItemFlingEffect.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemFlingEffectFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadData()
        setAdapter()
        setDataViews()
        setUI()
        setButtons()
    }



    private fun setDataViews() {

        binding.errorContainer.setOnClickListener {
            downloadData()
        }

        viewModel.loadingStatusItemFlingEffect.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idItemFlingEffectDetailContainer.visibility = View.GONE
                    binding.itemFlingEffectItemCategory.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemFlingEffectDetailContainer.visibility = View.VISIBLE
                    binding.itemFlingEffectItemCategory.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemFlingEffectDetailContainer.visibility = View.GONE
                    binding.itemFlingEffectItemCategory.visibility = View.GONE

                }
            }
        }


    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val flingEffectId = args.itemFlingEffectId.removePrefix("https://pokeapi.co/api/v2/item-fling-effect/").dropLast(1)
            viewModel.downloadItemFlingEffect(flingEffectId)
        }
    }

    private fun setUI() {
        viewModel.itemFlingEffectDetailResponse.observe(viewLifecycleOwner) {
            binding.idItemFlingEffectName.text = it.name.replace("-", " ")
            binding.idEffectValue.text = it.effectEntries[0].effect

            itemFlingEffectAdapter?.addItems(it.items)
        }
    }


    private fun setAdapter() {
        itemFlingEffectAdapter = ItemFlingEffectAdapter()
        binding.idRvItemFlingEffect.adapter = itemFlingEffectAdapter
        binding.idRvItemFlingEffect.layoutManager =
            GridLayoutManager(this.context, 2)


        itemFlingEffectAdapter!!.setOnItemClickListenerUrlFlingEffect {
            findNavController().navigate(
                ItemFlingEffectFragmentDirections.actionItemFlingEffectFragmentToItemPokedexDetailFragment(
                    it, false
                )
            )
        }
    }

    private fun setButtons() {

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        itemFlingEffectAdapter = null

        _binding = null
    }
}