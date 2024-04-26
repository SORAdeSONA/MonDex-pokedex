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
import com.sds.pokemonguide.adapter.ItemPocketAdapter
import com.sds.pokemonguide.databinding.ItemPocketFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemPocketFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: ItemPocketFragmentArgs by navArgs()

    private var itemPocketAdapter: ItemPocketAdapter? = null

    private var _binding: ItemPocketFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusItemPocket.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemPocketFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusItemPocket.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idItemPocketDetailContainer.visibility = View.GONE
                    binding.itemPocketItemCategory.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemPocketDetailContainer.visibility = View.VISIBLE
                    binding.itemPocketItemCategory.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemPocketDetailContainer.visibility = View.GONE
                    binding.itemPocketItemCategory.visibility = View.GONE

                }
            }
        }


    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val flingEffectId = args.itemPocketId.removePrefix("https://pokeapi.co/api/v2/item-pocket/").dropLast(1)
            viewModel.downloadItemPocket(flingEffectId)
        }
    }

    private fun setUI() {
        viewModel.itemPocketDetailResponse.observe(viewLifecycleOwner) {
            binding.idItemPocketName.text = it.name.replace("-", " ")

            itemPocketAdapter?.addCategories(it.categories)
        }
    }


    private fun setAdapter() {
        itemPocketAdapter = ItemPocketAdapter()
        binding.idRvItemPocket.adapter = itemPocketAdapter
        binding.idRvItemPocket.layoutManager =
            GridLayoutManager(this.context, 2)


        itemPocketAdapter!!.setOnItemClickListenerUrlPocket {
            findNavController().navigate(
                ItemPocketFragmentDirections.actionItemPocketFragmentToItemItemCategoryDetailFragment(
                    it
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
        itemPocketAdapter = null
        _binding = null
    }
}