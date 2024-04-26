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
import com.sds.pokemonguide.adapter.AttributeItemsAdapter
import com.sds.pokemonguide.adapter.ItemAttributeAdapter
import com.sds.pokemonguide.databinding.ItemAttributeFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemAttributeFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: ItemAttributeFragmentArgs by navArgs()

    private var itemAttributeAdapter: ItemAttributeAdapter? = null

    private var _binding: ItemAttributeFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusItemAttribute.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemAttributeFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusItemAttribute.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idItemAttributeDetailContainer.visibility = View.GONE
                    binding.itemAttributeItemCategory.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemAttributeDetailContainer.visibility = View.VISIBLE
                    binding.itemAttributeItemCategory.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idItemAttributeDetailContainer.visibility = View.GONE
                    binding.itemAttributeItemCategory.visibility = View.GONE

                }
            }
        }


    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val attributeId = args.itemAttributeId.removePrefix("https://pokeapi.co/api/v2/item-attribute/").dropLast(1)
            viewModel.downloadItemAttribute(attributeId)
        }
    }

    private fun setUI() {
        viewModel.itemAttributeDetailResponse.observe(viewLifecycleOwner) {
            binding.idItemAttributeName.text = it.name.replace("-", " ")
            binding.idDescriptionValue.text = it.descriptions[0].description

            itemAttributeAdapter?.addItems(it.items)
        }
    }


    private fun setAdapter() {
        itemAttributeAdapter = ItemAttributeAdapter()
        binding.idRvItemAttribute.adapter = itemAttributeAdapter
        binding.idRvItemAttribute.layoutManager =
            GridLayoutManager(this.context, 2)


        itemAttributeAdapter!!.setOnItemClickListenerUrlAttribute {
            findNavController().navigate(
                ItemAttributeFragmentDirections.actionItemAttributeFragmentToItemPokedexDetailFragment(
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

        itemAttributeAdapter = null

        _binding = null
    }
}