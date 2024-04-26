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
import com.sds.pokemonguide.adapter.BerryFlavorAdapter
import com.sds.pokemonguide.adapter.GenderAdapter
import com.sds.pokemonguide.databinding.GenderDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GenderDetailFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: GenderDetailFragmentArgs by navArgs()

    private var genderAdapter: GenderAdapter? = null

    private var _binding: GenderDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusGender.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GenderDetailFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusGender.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idGenderName.visibility = View.GONE
                    binding.genderItemCategory.visibility = View.GONE
                    binding.genderDetailContainer.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idGenderName.visibility = View.VISIBLE
                    binding.genderItemCategory.visibility = View.VISIBLE
                    binding.genderDetailContainer.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idGenderName.visibility = View.GONE
                    binding.genderItemCategory.visibility = View.GONE
                    binding.genderDetailContainer.visibility = View.GONE
                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.genderId.removePrefix("https://pokeapi.co/api/v2/gender/").dropLast(1)
            viewModel.downloadPokemonGender(id)
        }
    }

    private fun setUI() {
        viewModel.genderDetailResponse.observe(viewLifecycleOwner) {
            binding.idGenderName.text = it.name

            genderAdapter?.addPokemons(it.pokemonSpeciesDetailList)
        }
    }

    private fun setAdapter() {
        genderAdapter = GenderAdapter()

        binding.idRvGenderPokemons.adapter = genderAdapter
        binding.idRvGenderPokemons.layoutManager =
            GridLayoutManager(this.context, 2)

        genderAdapter!!.setOnItemClickListenerUrlGender{
            findNavController().navigate(
                GenderDetailFragmentDirections.actionGenderDetailPokedexFragmentToPokemonDetailPokedexFragment(
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

        genderAdapter = null

        _binding = null
    }
}