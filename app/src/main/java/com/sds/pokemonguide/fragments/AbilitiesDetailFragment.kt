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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sds.pokemonguide.adapter.AbilityUsedByPokemonsAdapter
import com.sds.pokemonguide.databinding.AbilityDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AbilitiesDetailFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: AbilitiesDetailFragmentArgs by navArgs()

    private var abilityUsedByPokemonsAdapter: AbilityUsedByPokemonsAdapter? = null

    private var _binding: AbilityDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusAbility.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AbilityDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadData()
        setDataViews()
        setUI()
        setAdapter()
        setButtons()
    }

    private fun setDataViews() {

        binding.errorContainer.setOnClickListener {
            downloadData()
        }


        viewModel.loadingStatusAbility.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.idPokemonAbilityName.visibility = View.GONE
                    binding.moveItemCategory.visibility = View.GONE
                    binding.idAbilityDetailContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE

                }

                LoadingStatus.SUCCESS -> {
                    binding.idPokemonAbilityName.visibility = View.VISIBLE
                    binding.moveItemCategory.visibility = View.VISIBLE
                    binding.idAbilityDetailContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE


                }

                LoadingStatus.ERROR -> {
                    binding.idPokemonAbilityName.visibility = View.GONE
                    binding.moveItemCategory.visibility = View.GONE
                    binding.idAbilityDetailContainer.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE

                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            if (args.isSearch) {
                viewModel.downloadAbilityDetailsById(args.abilityId)
            } else {
                val abilityId =
                    args.abilityId.removePrefix("https://pokeapi.co/api/v2/ability/").dropLast(1)
                viewModel.downloadAbilityDetailsById(abilityId)
            }

        }
    }

    private fun setUI() {
        viewModel.abilityDetailResponse.observe(viewLifecycleOwner) {
            binding.idPokemonAbilityName.text = it.name.replace("-", " ")
            binding.idAbilityIsMainSeriesValue.text = returnIsMainSeriesValue(it.isMainSeries)
            binding.idAbilityGenerationValue.text =
                it.generation.generationName.removePrefix("generation-")


            for (value in it.flavorTextEntriesAbility) {
                println(value.language.languageName)
                if (value.language.languageName == "en") binding.idFlavorTextAbility.text =
                    value.flavorText
            }

            for (value in it.effectEntriesAbility) {
                println(value.language.languageName)
                if (value.language.languageName == "en") binding.idAbilityEffectValue.text =
                    value.shortEffect
            }

            //TODO
            abilityUsedByPokemonsAdapter?.addAlsoUsedByPokemons(it.pokemonsWithAbility)
        }
    }

    private fun returnIsMainSeriesValue(value: Boolean): String {
        return if (value) "yes" else "no"
    }


    private fun setAdapter() {
        abilityUsedByPokemonsAdapter = AbilityUsedByPokemonsAdapter()

        binding.idAbilityRvPokemons.adapter = abilityUsedByPokemonsAdapter
        binding.idAbilityRvPokemons.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)


        abilityUsedByPokemonsAdapter!!.setOnItemClickListenerUrlAbility {
            findNavController().navigate(
                AbilitiesDetailFragmentDirections.actionAttributePokedexFragmentToDetailPokedexFragment(
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
        abilityUsedByPokemonsAdapter = null
        _binding = null
    }

}