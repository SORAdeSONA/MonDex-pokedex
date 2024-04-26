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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.adapter.AttributeItemsAdapter
import com.sds.pokemonguide.adapter.ItemAttributesAdapter
import com.sds.pokemonguide.adapter.ItemHeldByAdapter
import com.sds.pokemonguide.databinding.AttributeFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AttributesFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: AttributesFragmentArgs by navArgs()

    private var attributesItemsAdapter: AttributeItemsAdapter? = null

    private var _binding: AttributeFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusAttribute.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AttributeFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusAttribute.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idMoveDetailContainer.visibility = View.GONE
                    binding.attributeItemCategory.visibility = View.GONE


                }

                LoadingStatus.SUCCESS -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveDetailContainer.visibility = View.VISIBLE
                    binding.attributeItemCategory.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveDetailContainer.visibility = View.GONE
                    binding.attributeItemCategory.visibility = View.GONE

                }
            }
        }


    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val attributeId =
                args.attributeId.removePrefix("https://pokeapi.co/api/v2/item-attribute/")
                    .dropLast(1)
            viewModel.downloadAttributeDetailsById(attributeId)
        }
    }

    private fun setUI() {
        viewModel.attributeDetailResponse.observe(viewLifecycleOwner) {
            binding.idPokemonAttributeName.text = it.name.replace("-", " ")
            binding.idAttributeDescriptionValue.text = it.descriptions[0].description

            attributesItemsAdapter?.addItemsWithAttribute(it.items)
        }
    }


    private fun setAdapter() {
        attributesItemsAdapter = AttributeItemsAdapter()
        binding.idRvAttributesItems.adapter = attributesItemsAdapter
        binding.idRvAttributesItems.layoutManager =
            GridLayoutManager(this.context, 2)


        attributesItemsAdapter!!.setOnItemClickListenerAttributeItem {
            findNavController().navigate(
                AttributesFragmentDirections.actionAttributePokedexFragmentToItemPokedexFragment(
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
        attributesItemsAdapter = null
        _binding = null
    }
}