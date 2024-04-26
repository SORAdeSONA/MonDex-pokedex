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
import com.sds.pokemonguide.adapter.ItemAttributeAdapter
import com.sds.pokemonguide.adapter.ItemCategoryAdapter
import com.sds.pokemonguide.databinding.ItemCategoryFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemCategoryFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: ItemCategoryFragmentArgs by navArgs()

    private var itemCategoryAdapter: ItemCategoryAdapter? = null

    private var _binding: ItemCategoryFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusItemCategory.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemCategoryFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusItemCategory.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idItemCategoryDetailContainer.visibility = View.GONE
                    binding.itemCategoryItemCategory.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemCategoryDetailContainer.visibility = View.VISIBLE
                    binding.itemCategoryItemCategory.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemCategoryDetailContainer.visibility = View.GONE
                    binding.itemCategoryItemCategory.visibility = View.GONE

                }
            }
        }


    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val attributeId = args.itemCategoryId.removePrefix("https://pokeapi.co/api/v2/item-category/").dropLast(1)
            viewModel.downloadItemCategory(attributeId)
        }
    }

    private fun setUI() {
        viewModel.itemCategoryDetailResponse.observe(viewLifecycleOwner) { response ->
            binding.idItemCategoryName.text = response.name.replace("-", " ")
            binding.idCategoryPocketValue.text = response.pocket.name + " \uD83D\uDD0E"

            binding.idCategoryPocketValue.setOnClickListener {
                findNavController().navigate(
                    ItemCategoryFragmentDirections.actionItemCategoryFragmentToPocketPokedexDetailFragment(
                        response.pocket.url
                    )
                )
            }

            itemCategoryAdapter?.addItems(response.items)
        }
    }


    private fun setAdapter() {
        itemCategoryAdapter = ItemCategoryAdapter()
        binding.idRvItemCategory.adapter = itemCategoryAdapter
        binding.idRvItemCategory.layoutManager =
            GridLayoutManager(this.context, 2)


        itemCategoryAdapter!!.setOnItemClickListenerUrlCategory {
            findNavController().navigate(
                ItemCategoryFragmentDirections.actionItemCategoryFragmentToItemPokedexDetailFragment(
                    it, false
                )
            )
        }
    }

    private fun setButtons(){

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        itemCategoryAdapter = null

        _binding = null
    }
}