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
import com.sds.pokemonguide.adapter.EggGroupDetailAdapter
import com.sds.pokemonguide.databinding.EggGroupFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EggGroupFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: EggGroupFragmentArgs by navArgs()

    private var eggGroupAdapter: EggGroupDetailAdapter? = null

    private var _binding: EggGroupFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusEggGroup.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EggGroupFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusEggGroup.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                    binding.idEggGroup.visibility = View.GONE
                    binding.eggGroupItemCategory.visibility = View.GONE
                    binding.idEggGroupDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idEggGroup.visibility = View.VISIBLE
                    binding.eggGroupItemCategory.visibility = View.VISIBLE
                    binding.idEggGroupDetailContainer.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idEggGroup.visibility = View.GONE
                    binding.eggGroupItemCategory.visibility = View.GONE
                    binding.idEggGroupDetailContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.eggGroupId.removePrefix("https://pokeapi.co/api/v2/egg-group/").dropLast(1)
            viewModel.downloadEggGroup(id)
        }
    }

    private fun setUI() {
        viewModel.eggGroupDetailResponse.observe(viewLifecycleOwner) {
            binding.idEggGroup.text = it.name.replace("-", " ")

            eggGroupAdapter?.addPokemons(it.pokemonSpecies)
        }
    }

    private fun setAdapter() {
        eggGroupAdapter = EggGroupDetailAdapter()

        binding.idRvEggGroupPokemons.adapter = eggGroupAdapter
        binding.idRvEggGroupPokemons.layoutManager =
            GridLayoutManager(this.context, 2)


        eggGroupAdapter!!.setOnItemClickListenerUrlEggGroup {
            findNavController().navigate(
                EggGroupFragmentDirections.actionEggGroupFragmentToPokedexDetailFragment(
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

        eggGroupAdapter = null

        _binding = null
    }
}